package com.firefly.layers.layers;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.derivative.operation.Var;
import com.firefly.layers.core.Layer;
import com.firefly.layers.data.*;
import com.firefly.layers.enums.Padding;
import com.firefly.layers.init.params.InitParamsRandomOrdinary;
import com.firefly.layers.listeners.InitParamsListener;
import com.firefly.math.Binomial;
import com.firefly.math.ConvUtil;

/**
 * 卷积层
 */
public class Conv implements Layer {
    private static float KEEP_PROB_DEFAULT=1.0F;

    private ThreeDimShape inputShape;//输入形状
    private ThreeDimShape inputShapeExpand;//输入参数扩大的形状
    private ThreeDimShape unitShape;//输出单元数
    private int filters;
    private int kernelWidth;
    private int kernelHeight;
    private int strides;
    private Padding padding;

    private Class<? extends OperationActivation> activationCls;//激活函数类

    private MultiDim activationSettingsMd;
    private Function[] activationSettings;

    private InitParamsListener initParamsListener;//初始化参数事件
    private float keepProb=KEEP_PROB_DEFAULT;//节点保留概率，dropout功能

    private MultiDim wmd;
    private MultiDim bmd;
    private double[][][][] w;
    private double[] b;
    private double[][][][] diffW;
    private double[] diffB;

    private MultiDim wxbmd;
    private Var[][][] wxb;
    private OperationActivation[][][] outs;

    public Conv(){

    }

    public Conv(
            int filters,
            int kernelSize,
            int strides, Padding padding,
            Class<? extends OperationActivation> activationCls, float keepProb, Function[] activationSettings, InitParamsListener initParamsListener){
        this(filters,kernelSize,kernelSize,strides,padding,activationCls,keepProb,activationSettings,initParamsListener);
    }

    public Conv(
            int filters,
            int kernelWidth,
            int kernelHeight,
            int strides, Padding padding,
            Class<? extends OperationActivation> activationCls, float keepProb, Function[] activationSettings, InitParamsListener initParamsListener){
        this(null,filters,kernelWidth,kernelHeight,strides,padding,activationCls,keepProb,activationSettings,initParamsListener);
    }

    public Conv(
            ThreeDimShape inputShape,
            int filters,
            int kernelSize,
            int strides, Padding padding,
            Class<? extends OperationActivation> activationCls, float keepProb, Function[] activationSettings, InitParamsListener initParamsListener){
        this(inputShape,filters,kernelSize,kernelSize,strides,padding,activationCls,keepProb,activationSettings,initParamsListener);
    }

    /**
     *
     * @param inputShape 输入形状
     * @param filters 卷积核（就是过滤器！）的数目（即输出的维度）
     * @param kernelWidth
     * @param kernelHeight 单个整数或由两个整数构成的list/tuple，卷积核（过滤器）的宽度和长度。（kernel n.核心，要点，[计]内核） 如为单个整数，则表示在各个空间维度的相同长度。
     * @param strides 单个整数或由两个整数构成的list/tuple，为卷积的步长。
     * @param padding 补0策略，为"valid", "same" 。
     *                "valid"不填充，eg:图像28*28，过滤器5*5，步长为5，最后三行三列舍弃，输出大小为：[（28-3-5）/5]+1=5，即输出图像是5*5的
     * 代表只进行有效的卷积，即对边界数据不处理。
     *                "same"填充，当滑动步长大于1时：填充数=K-I%S（K:卷积核边长，I：输入图像边长，S：滑动步长），
     * 滑动步长为1时，填充数是卷积核边长减1，eg:5*5的图用3*3的核，步长为1时same填充之后是7*7
     * 代表保留边界处的卷积结果，
     * 通常会导致输出shape与输入shape相同，因为卷积核移动时在边缘会出现大小不够的情况。
     * @param activationCls
     * @param keepProb
     * @param activationSettings
     * @param initParamsListener
     */
    public Conv(
            ThreeDimShape inputShape,
            int filters,
            int kernelWidth,
            int kernelHeight,
            int strides, Padding padding,
            Class<? extends OperationActivation> activationCls, float keepProb, Function[] activationSettings, InitParamsListener initParamsListener){
        this.inputShape=inputShape;
        this.filters=filters;
        this.kernelWidth=kernelWidth;
        this.kernelHeight=kernelHeight;
        this.strides=strides;
        this.padding=padding;
        this.activationCls=activationCls;
        this.keepProb=keepProb;
        this.activationSettings=activationSettings;
        if(activationSettings!=null){
            this.activationSettingsMd=new MultiDim(Function.class,new Shape(new int[]{activationSettings.length}),activationSettings);
        }
        this.initParamsListener=initParamsListener;
    }

    /**
     * 计算输出维度
     * @param inputShape 输入形状
     * @param kernelWidth
     * @param kernelHeight 单个整数或由两个整数构成的list/tuple，卷积核（过滤器）的宽度和长度。（kernel n.核心，要点，[计]内核） 如为单个整数，则表示在各个空间维度的相同长度。
     * @param strides 单个整数或由两个整数构成的list/tuple，为卷积的步长。
     * @param padding 补0策略，为"valid", "same" 。
     *                "valid"不填充，eg:图像28*28，过滤器5*5，步长为5，最后三行三列舍弃，输出大小为：[（28-3-5）/5]+1=5，即输出图像是5*5的
     * 代表只进行有效的卷积，即对边界数据不处理。
     *                "same"填充，当滑动步长大于1时：填充数=K-I%S（K:卷积核边长，I：输入图像边长，S：滑动步长），
     * 滑动步长为1时，填充数是卷积核边长减1，eg:5*5的图用3*3的核，步长为1时same填充之后是7*7
     * 代表保留边界处的卷积结果，
     * 通常会导致输出shape与输入shape相同，因为卷积核移动时在边缘会出现大小不够的情况。
     * @return
     */
    private ThreeDimShape getInputExpandDim(ThreeDimShape inputShape,
                                            int kernelWidth,int kernelHeight,
                                            int strides, Padding padding){
        ThreeDimShape ret=null;//经过padding后的维度
        switch (padding){
            case valid:
                ret= ConvUtil.getConvNumValid(inputShape,kernelWidth,kernelHeight,strides);
                break;
            case same:
                ret= ConvUtil.getConvNumSame(inputShape,kernelWidth,kernelHeight,strides);
                break;
        }
        return ret;
    }

    /**
     *
     * @param inputShape
     * @param filters 卷积核（就是过滤器！）的数目（即输出的维度）
     * @param strides
     * @return
     */
    private ThreeDimShape getOutDim(ThreeDimShape inputShape,int filters,int kernelWidth,int kernelHeight,int strides){
        int x=(inputShape.getX()-kernelWidth)/strides+1;
        int y=(inputShape.getY()-kernelHeight)/strides+1;

        return new ThreeDimShape(filters,x,y);
    }

    public Shape getInputShape() {
        return inputShape;
    }

    public void setInputShape(Shape inputShape) {
        this.inputShape = new ThreeDimShape(inputShape);
    }

    public Shape getUnitShape() {
        return unitShape;
    }

    public void setUnitShape(Shape unitShape) {
        this.unitShape = (ThreeDimShape) unitShape;
    }

    public MultiDim getW() {
        return wmd;
    }

    public void setW(MultiDim w) {
        this.wmd = w;
        this.w=(double[][][][])w.getData();
    }

    public MultiDim getB() {
        return bmd;
    }

    public void setB(MultiDim b) {
        this.bmd = b;
        this.b=(double[])b.getData();
    }

    @Override
    public void init() {
        //计算输入扩大的形状
        this.inputShapeExpand=getInputExpandDim(this.inputShape,kernelWidth,kernelHeight,strides,padding);
        this.unitShape=getOutDim(inputShapeExpand,filters,kernelWidth,kernelHeight,strides);

        //w的过滤器、卷积核宽、卷积核高，输入的第3个维度
        wmd=new MultiDim(Double.TYPE,new FourDimShape(filters,kernelWidth,kernelHeight,inputShape.getZ()));
        bmd=new MultiDim(Double.TYPE,new OneDimShape(filters));
        w=(double[][][][]) wmd.getData();
        b=(double[]) bmd.getData();

        diffW=new double[filters][kernelWidth][kernelHeight][inputShape.getZ()];
        diffB=new double[filters];

        //初始化神经元函数
        initFunc();

        //随机初始化参数
        randomInitParmas(initParamsListener);
    }

    private void randomInitParmas(InitParamsListener initParamsListener){
        if(initParamsListener==null){
            initParamsListener=new InitParamsRandomOrdinary();
        }
        //设置初始化参数事件的大小
        initParamsListener.paramWSize(this.wmd.getShape());
        initParamsListener.paramBSize(this.bmd.getShape());

        ShapeIndex wsi=new ShapeIndex(new int[]{0,0,0,0});
        ShapeIndex bsi=new ShapeIndex(new int[]{0});

        for(int i=0;i<filters;i++){
            for(int j=0;j<kernelWidth;j++){
                for(int k=0;k<kernelHeight;k++){
                    for(int l=0;l<inputShape.getZ();l++){
                        wsi.setDimIndexVal(0,i);
                        wsi.setDimIndexVal(1,j);
                        wsi.setDimIndexVal(2,k);
                        wsi.setDimIndexVal(3,l);

                        this.w[i][j][k][l]=initParamsListener.initParamW(wsi);
                    }
                }

                bsi.setDimIndexVal(0,i);
                this.b[i]=initParamsListener.initParamB(bsi);
            }
        }
    }

    private void initFunc(){
        outs=new OperationActivation[unitShape.getX()][unitShape.getY()][unitShape.getZ()];//输出单元数

        wxbmd=new MultiDim(Var.class,unitShape);
        wxb=(Var[][][]) wxbmd.getData();

        for(int i=0;i<outs.length;i++){
            for(int j=0;j<outs[i].length;j++){
                for(int k=0;k<outs[i][j].length;k++){
                    try {
                        wxb[i][j][k]=new Var();
                        outs[i][j][k]=activationCls.newInstance();
                        //激活函数设置值
                        outs[i][j][k].setSettings(activationSettingsMd);
                        //设置当前数据
                        outs[i][j][k].setVal(wxb[i][j][k]);
                        //设置相关的数据
                        outs[i][j][k].setRelations(wxbmd);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void calc(MultiDim input,MultiDim out) {
        double[][][] outVal=(double[][][])out.getData();

        input=ConvUtil.expand(input,inputShapeExpand);
        //计算卷积
        MultiDim conv=ConvUtil.conv(input,wmd,bmd,keepProb,strides,unitShape);
        double[][][] convData=(double[][][])conv.getData();

        for(int x=0;x<convData.length;x++){
            for(int y=0;y<convData[x].length;y++){
                for(int z=0;z<convData[x][y].length;z++){
                    //将值赋给变量
                    wxb[x][y][z].setVal(convData[x][y][z]);
                    //经过激活函数计算返回值
                    outVal[x][y][z]=this.outs[x][y][z].calc();
                }
            }
        }
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {
        for(int w=0;w<diffW.length;w++){
            for(int x=0;x<diffW[w].length;x++){
                for(int y=0;y<diffW[w][x].length;y++){
                    for(int z=0;z<diffW[w][x][y].length;z++){
                        //重置w的更新梯度
                        diffW[w][x][y][z]=0;
                    }
                }
            }
            //计算b的更新梯度
            diffB[w]=0;
        }
    }

    @Override
    public void addBackUpdateParamPrtGrad(MultiDim input,MultiDim targetVal,MultiDim outFrontLayerPrtGrad,MultiDim backLayerPrtGrad) {
        input=ConvUtil.expand(input,inputShapeExpand);
        double[][][]inputVal=(double[][][])input.getData();

        double[][][] backLayerPrtGradVal=(double[][][])backLayerPrtGrad.getData();

        MultiDim dloss_dwxb_md=new MultiDim(this.unitShape);
        double[][][] dloss_dwxb=(double[][][])dloss_dwxb_md.getData();
        int[][][] binomial= (int[][][])Binomial.binomialOfInt(keepProb,this.unitShape).getData();//二项分布

        //计算（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
        calcDLossDWxb(backLayerPrtGradVal,outs,wxb,binomial,targetVal,dloss_dwxb);

        //计算（损失函数/激活函数）*（激活函数/w）的偏导梯度
        calcDLossDWs(dloss_dwxb,inputVal,diffW,strides);

        //计算（损失函数/激活函数）*（激活函数/b）的偏导梯度
        calcDLossDBs(dloss_dwxb,inputVal,diffB);

        //累计输入参数的更新值
        if(outFrontLayerPrtGrad!=null){
            double[] outFrontLayerPrtGradVal=(double[])outFrontLayerPrtGrad.getData();

//            //计算输入值的更新梯度
//            double[] cpt=Linalg.inner(w,dloss_dwxb,true);
//            for(int i=0;i<currentPrtGradVal.length;i++){
//                currentPrtGradVal[i]+=cpt[i];
//            }
        }
    }

    /**
     * 计算（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
     * @param backLayerPrtGradVal
     * @param outs
     * @param wxb
     * @param targetVal
     * @param out_dloss_dwxb
     */
    private void calcDLossDWxb(double[][][] backLayerPrtGradVal,OperationActivation[][][] outs,Var[][][] wxb,int[][][] binomial,MultiDim targetVal,double[][][] out_dloss_dwxb){
        //计算（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
        for(int x=0;x<out_dloss_dwxb.length;x++){
            for(int y=0;y<out_dloss_dwxb[x].length;y++){
                for(int z=0;z<out_dloss_dwxb[x][y].length;z++){
                    if(backLayerPrtGradVal[x][y][z]!=0){
                        if(binomial[x][y][z]!=0){
                            out_dloss_dwxb[x][y][z]=backLayerPrtGradVal[x][y][z]*binomial[x][y][z]*outs[x][y][z].prtGrad(wxb[x][y][z],targetVal);//（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算（损失函数/激活函数）*（激活函数/所有w）的偏导梯度
     * @param dloss_dwxb
     * @param input
     * @param out_dloss_dw
     */
    private void calcDLossDWs(double[][][] dloss_dwxb,double[][][] input,double[][][][] out_dloss_dw,int strides){
        //计算（损失函数/激活函数）*（激活函数/所有w）的偏导梯度
        for(int w=0;w<out_dloss_dw.length;w++){
            for(int x=0;x<out_dloss_dw[w].length;x++){
                for(int y=0;y<out_dloss_dw[w][x].length;y++){
                    for(int z=0;z<out_dloss_dw[w][x][y].length;z++){
                        //计算单个损失函数/权重的梯度
                        out_dloss_dw[w][x][y][z]+=calcDLossDW(dloss_dwxb,input,w,x,y,z,strides);
                    }
                }
            }
        }
    }

    /**
     * 计算（损失函数/激活函数）*（激活函数/w）的偏导梯度
     * @param dloss_dwxb
     * @param input
     * @param w
     * @param x
     * @param y
     * @param z
     * @return
     */
    private double calcDLossDW(double[][][] dloss_dwxb,double[][][] input,int w,int x,int y,int z,int strides){
        double ret=0;

        double[][] dloss_dwxb_filter=dloss_dwxb[w];//第几通道的梯度
        //循环指定过滤器的所有（损失函数/wxb）梯度
        for(int i=0;i<dloss_dwxb_filter.length;i++){
            for(int j=0;j<dloss_dwxb_filter[i].length;j++){
                ret+=dloss_dwxb[w][i][j]*input[(i*strides)+x][(j*strides)+y][z];
            }
        }

        return ret;
    }

    /**
     * 计算（损失函数/激活函数）*（激活函数/所有b）的偏导梯度
     * @param dloss_dwxb
     * @param input
     * @param out_dloss_db
     */
    private void calcDLossDBs(double[][][] dloss_dwxb,double[][][] input,double[] out_dloss_db){
        //计算（损失函数/激活函数）*（激活函数/所有b）的偏导梯度
        for(int w=0;w<out_dloss_db.length;w++){
            out_dloss_db[w]=calcDLossDB(dloss_dwxb,input,w);
        }
    }

    /**
     * 计算（损失函数/激活函数）*（激活函数/w）的偏导梯度
     * @param dloss_dwxb
     * @param input
     * @param w
     * @return
     */
    private double calcDLossDB(double[][][] dloss_dwxb,double[][][] input,int w){
        double ret=0;

        double[][] dloss_dwxb_filter=dloss_dwxb[w];//第几通道的梯度
        //循环指定过滤器的所有（损失函数/wxb）梯度
        for(int i=0;i<dloss_dwxb_filter.length;i++){
            for(int j=0;j<dloss_dwxb_filter[i].length;j++){
                ret+=dloss_dwxb[w][i][j];
            }
        }

        return ret;
    }

    @Override
    public void flushBackUpdateParamPrtGrad(double rate) {
        //计算（损失函数/激活函数）*（激活函数/所有w）的偏导梯度
        for(int w=0;w<diffW.length;w++){
            for(int x=0;x<diffW[w].length;x++){
                for(int y=0;y<diffW[w][x].length;y++){
                    for(int z=0;z<diffW[w][x][y].length;z++){
                        this.w[w][x][y][z]-=rate*diffW[w][x][y][z];
                    }
                }
            }
            //计算b的更新梯度
            b[w]-=rate*diffB[w];
        }
    }

}
