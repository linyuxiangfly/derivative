package com.firefly.layers.layers;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.derivative.operation.Var;
import com.firefly.layers.core.Layer;
import com.firefly.layers.data.*;
import com.firefly.layers.enums.Padding;
import com.firefly.layers.init.params.InitParamsRandomOrdinary;
import com.firefly.layers.listeners.InitParamsListener;
import com.firefly.math.ConvUtil;

/**
 * 卷积层
 */
public class Conv implements Layer {
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
            Class<? extends OperationActivation> activationCls, Function[] activationSettings, InitParamsListener initParamsListener){
        this(filters,kernelSize,kernelSize,strides,padding,activationCls,activationSettings,initParamsListener);
    }

    public Conv(
            int filters,
            int kernelWidth,
            int kernelHeight,
            int strides, Padding padding,
            Class<? extends OperationActivation> activationCls, Function[] activationSettings, InitParamsListener initParamsListener){
        this(null,filters,kernelWidth,kernelHeight,strides,padding,activationCls,activationSettings,initParamsListener);
    }

    public Conv(
            ThreeDimShape inputShape,
            int filters,
            int kernelSize,
            int strides, Padding padding,
            Class<? extends OperationActivation> activationCls, Function[] activationSettings, InitParamsListener initParamsListener){
        this(inputShape,filters,kernelSize,kernelSize,strides,padding,activationCls,activationSettings,initParamsListener);
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
     * @param activationSettings
     * @param initParamsListener
     */
    public Conv(
            ThreeDimShape inputShape,
            int filters,
            int kernelWidth,
            int kernelHeight,
            int strides, Padding padding,
            Class<? extends OperationActivation> activationCls, Function[] activationSettings, InitParamsListener initParamsListener){
        this.inputShape=inputShape;
        this.filters=filters;
        this.kernelWidth=kernelWidth;
        this.kernelHeight=kernelHeight;
        this.strides=strides;
        this.padding=padding;
        this.activationCls=activationCls;
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

        return new ThreeDimShape(x,y,filters);
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
        wmd=new MultiDim(Double.TYPE,new FourDimShape(kernelWidth,kernelHeight,inputShape.getZ(),filters));
        bmd=new MultiDim(Double.TYPE,new OneDimShape(filters));
        w=(double[][][][]) wmd.getData();
        b=(double[]) bmd.getData();

        diffW=new double[kernelWidth][kernelHeight][inputShape.getZ()][filters];
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
                        wsi.setDimIndexVal(0,j);
                        wsi.setDimIndexVal(1,k);
                        wsi.setDimIndexVal(2,l);
                        wsi.setDimIndexVal(3,i);

                        this.w[j][k][l][i]=initParamsListener.initParamW(wsi);
                    }
                }
            }
            bsi.setDimIndexVal(0,i);
            this.b[i]=initParamsListener.initParamB(bsi);
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
        MultiDim conv=ConvUtil.conv(input,wmd,bmd,strides,unitShape);
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
        }

        for(int x=0;x<diffB.length;x++){
            //计算b的更新梯度
            diffB[x]=0;
        }
    }

    @Override
    public void addBackUpdateParamPrtGrad(MultiDim input,MultiDim targetVal,MultiDim outFrontLayerPrtGrad,MultiDim backLayerPrtGrad) {
        //扩展后的宽高
        int widthE=inputShapeExpand.getX();
        int heightE=inputShapeExpand.getY();
        //没扩展前的宽高
        int width=inputShape.getX();
        int height=inputShape.getY();
        //计算左边和上边的边界，如果扩展大后，左、右、上、下都加宽了
        int leftBorder=widthE<width?0:-(widthE-width)/2;
        int topBorder=heightE<height?0:-(heightE-height)/2;

        MultiDim inputExpand=ConvUtil.expand(input,inputShapeExpand);
        double[][][]inputExpandVal=(double[][][])inputExpand.getData();

        double[][][] backLayerPrtGradVal=(double[][][])backLayerPrtGrad.getData();

        MultiDim dloss_dwxb_md=new MultiDim(this.unitShape);
        double[][][] dloss_dwxb=(double[][][])dloss_dwxb_md.getData();

        //计算（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
        calcDLossDWxb(backLayerPrtGradVal,outs,wxb,targetVal,dloss_dwxb);

        //计算（损失函数/激活函数）*（激活函数/w）的偏导梯度
        calcDLossDWs(dloss_dwxb,inputExpandVal,kernelWidth,kernelHeight,inputShape.getZ(),leftBorder,topBorder,strides,diffW);

        //计算（损失函数/激活函数）*（激活函数/b）的偏导梯度
        calcDLossDBs(dloss_dwxb,inputExpandVal,diffB);

        //累计输入参数的更新值
        if(outFrontLayerPrtGrad!=null){
            double[][][] outFrontLayerPrtGradVal=(double[][][])outFrontLayerPrtGrad.getData();

            calcDLossDXs(dloss_dwxb,w,kernelWidth,kernelHeight,inputShape.getZ(),leftBorder,topBorder,strides,outFrontLayerPrtGradVal);
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
    private void calcDLossDWxb(double[][][] backLayerPrtGradVal,OperationActivation[][][] outs,Var[][][] wxb,MultiDim targetVal,double[][][] out_dloss_dwxb){
        //计算（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
        for(int x=0;x<out_dloss_dwxb.length;x++){
            for(int y=0;y<out_dloss_dwxb[x].length;y++){
                for(int z=0;z<out_dloss_dwxb[x][y].length;z++){
                    if(backLayerPrtGradVal[x][y][z]!=0){
                        out_dloss_dwxb[x][y][z]=backLayerPrtGradVal[x][y][z]*outs[x][y][z].prtGrad(wxb[x][y][z],targetVal);//（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
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
    private void calcDLossDWs(
            double[][][] dloss_dwxb,
            double[][][] input,
            int kernelWidth, int kernelHeight, int kernelChannel,
            int leftBorder,int topBorder,
            int strides,
            double[][][][] out_dloss_dw
            ){
        //循环指定过滤器的所有（损失函数/所有w）梯度
        for(int x=0;x<dloss_dwxb.length;x++){
            for(int y=0;y<dloss_dwxb[x].length;y++){
                for(int z=0;z<dloss_dwxb[x][y].length;z++){
                    double loss=dloss_dwxb[x][y][z];//当前损失梯度
                    //计算单个损失函数/输入值的梯度
                    calcDLossDW(
                            loss,
                            z,
                            input,
                            kernelWidth,kernelHeight,kernelChannel,
                            (x*strides)+leftBorder,(y*strides)+topBorder,
                            out_dloss_dw);
                }
            }
        }
    }

    /**
     * 计算（损失函数/激活函数）*（激活函数/w）的偏导梯度
     * @param dloss_dwxb
     * @param filter
     * @param input
     * @param kernelWidth
     * @param kernelHeight
     * @param kernelChannel
     * @param ox
     * @param oy
     * @param out_dloss_dw
     */
    private void calcDLossDW(
            double dloss_dwxb,
            int filter,
            double[][][] input,
            int kernelWidth, int kernelHeight, int kernelChannel,
            int ox,int oy,
            double[][][][] out_dloss_dw){
        //如果梯度不为0
        if(dloss_dwxb!=0){
            //循环指定过滤器的所有（损失函数/wxb）梯度
            for(int i=0;i<kernelWidth;i++){
                for(int j=0;j<kernelHeight;j++){
                    for(int k=0;k<kernelChannel;k++){
                        if((ox+i>=0 && oy+j>=0) && (ox+i<input.length && oy+j<input[0].length)){
                            //累计梯度
                            out_dloss_dw[i][j][k][filter]+=dloss_dwxb*input[ox+i][oy+j][k];
                        }
                    }
                }
            }
        }
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
            out_dloss_db[w]+=calcDLossDB(dloss_dwxb,input,w);
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

        //循环指定过滤器的所有（损失函数/wxb）梯度
        for(int i=0;i<dloss_dwxb.length;i++){
            for(int j=0;j<dloss_dwxb[i].length;j++){
                ret+=dloss_dwxb[i][j][w];
            }
        }

        return ret;
    }

    private void calcDLossDXs(
            double[][][] dloss_dwxb,
            double[][][][] weight,
            int kernelWidth, int kernelHeight, int kernelChannel,
            int leftBorder,int topBorder,
            int strides,
            double[][][] out_dloss_db){

        //计算（损失函数/激活函数）*（激活函数/所有x）的偏导梯度
        for(int x=0;x<dloss_dwxb.length;x++){
            for(int y=0;y<dloss_dwxb[x].length;y++){
                for(int z=0;z<dloss_dwxb[x][y].length;z++){
                    double loss=dloss_dwxb[x][y][z];//当前损失梯度
                    //计算单个损失函数/输入值的梯度
                    calcDLossDX(
                            loss,
                            z,
                            weight,
                            kernelWidth,kernelHeight,kernelChannel,
                            (x*strides)+leftBorder,(y*strides)+topBorder,
                            out_dloss_db);
                }
            }
        }
    }

    private void calcDLossDX(
            double dloss_dwxb,
            int filter,
            double[][][][] weight,
            int kernelWidth, int kernelHeight, int kernelChannel,
            int ox,int oy,
            double[][][] out_dloss_db){
        //如果梯度不为0
        if(dloss_dwxb!=0){
            //循环指定过滤器的所有（损失函数/wxb）梯度
            for(int i=0;i<kernelWidth;i++){
                for(int j=0;j<kernelHeight;j++){
                    for(int k=0;k<kernelChannel;k++){
                        if((ox+i>=0 && oy+j>=0) && (ox+i<out_dloss_db.length && oy+j<out_dloss_db[0].length)){
                            //累计梯度
                            out_dloss_db[ox+i][oy+j][k]+=dloss_dwxb*weight[i][j][k][filter];
                        }
                    }
                }
            }
        }
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
        }

        for(int x=0;x<diffB.length;x++){
            //计算b的更新梯度
            b[x]-=rate*diffB[x];
        }
    }

}
