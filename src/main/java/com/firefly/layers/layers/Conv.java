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
import com.firefly.math.Linalg;

/**
 * 卷积层
 */
public class Conv implements Layer {
    private static float KEEP_PROB_DEFAULT=1.0F;

    private ThreeDimShape inputShape;//输入形状
    private ThreeDimShape inputShapeExpand;//输入参数扩大的形状
    private ThreeDimShape unitShape;//输出单元数
    private int filters;
    private int kernelSize;
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

    /**
     *
     * @param inputShape 输入形状
     * @param filters 卷积核（就是过滤器！）的数目（即输出的维度）
     * @param kernelSize 单个整数或由两个整数构成的list/tuple，卷积核（过滤器）的宽度和长度。（kernel n.核心，要点，[计]内核） 如为单个整数，则表示在各个空间维度的相同长度。
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
            int filters, int kernelSize,
            int strides, Padding padding,
            Class<? extends OperationActivation> activationCls, float keepProb, Function[] activationSettings, InitParamsListener initParamsListener){
        this.inputShape=inputShape;
        this.filters=filters;
        this.kernelSize=kernelSize;
        this.strides=strides;
        this.padding=padding;
        this.activationCls=activationCls;
        this.keepProb=keepProb;
        this.activationSettings=activationSettings;
        if(activationSettings!=null){
            this.activationSettingsMd=new MultiDim(Function.class,new Shape(new int[]{activationSettings.length}),activationSettings);
        }
        this.initParamsListener=initParamsListener;

        //计算输入扩大的形状
        this.inputShapeExpand=getInputExpandDim(inputShape,kernelSize,strides,padding);
        this.unitShape=getOutDim(inputShapeExpand,filters,kernelSize,strides);
    }

    /**
     * 计算输出维度
     * @param inputShape 输入形状
     * @param kernelSize 单个整数或由两个整数构成的list/tuple，卷积核（过滤器）的宽度和长度。（kernel n.核心，要点，[计]内核） 如为单个整数，则表示在各个空间维度的相同长度。
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
    private ThreeDimShape getInputExpandDim(ThreeDimShape inputShape, int kernelSize,
                                    int strides, Padding padding){
        ThreeDimShape ret=null;//经过padding后的维度
        switch (padding){
            case valid:
                ret= ConvUtil.getConvNumValid(inputShape,kernelSize,strides);
                break;
            case same:
                ret= ConvUtil.getConvNumSame(inputShape,kernelSize,strides);
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
    private ThreeDimShape getOutDim(ThreeDimShape inputShape,int filters,int kernelSize,int strides){
        int x=(inputShape.getX()-kernelSize)/strides+1;
        int y=(inputShape.getY()-kernelSize)/strides+1;

        return new ThreeDimShape(filters,x,y);
    }

    public Shape getInputShape() {
        return inputShape;
    }

    public void setInputShape(Shape inputShape) {
        this.inputShape = (ThreeDimShape) inputShape;
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
        //w的过滤器、卷积核宽、卷积核高，输入的第3个维度
        wmd=new MultiDim(Double.TYPE,new FourDimShape(filters,kernelSize,kernelSize,inputShape.getZ()));
        bmd=new MultiDim(Double.TYPE,new OneDimShape(filters));
        w=(double[][][][]) wmd.getData();
        b=(double[]) bmd.getData();

        diffW=new double[filters][kernelSize][kernelSize][inputShape.getZ()];
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
            for(int j=0;j<kernelSize;j++){
                for(int k=0;k<kernelSize;k++){
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

    private double[][][] one2ThreeDim(double[] data,ThreeDimShape shape){
        return one2ThreeDim(data,shape.getX(),shape.getY(),shape.getZ());
    }

    /**
     * 1维数据转3维
     * @param data
     * @return
     */
    private double[][][] one2ThreeDim(double[] data,int height,int width,int rgb){
        double[][][] ret=new double[height][width][rgb];
        for(int x=0;x<height;x++){
            for(int y=0;y<width;y++){
                for(int z=0;z<rgb;z++){
                    ret[x][y][z]=data[x*width*rgb+y*rgb+z];
                }
            }
        }
        return ret;
    }

    @Override
    public void addBackUpdateParamPrtGrad(MultiDim prtGrad, MultiDim input,MultiDim targetVal,MultiDim currentPrtGrad) {
        double[][][]inputVal=(double[][][])input.getData();

        double[][][] prtGradVal;
        if(prtGrad.getShape().getDims().length==1){
            prtGradVal=one2ThreeDim((double[])prtGrad.getData(),unitShape);
        }else{
            prtGradVal=(double[][][])prtGrad.getData();
        }

        MultiDim dloss_dwxb_md=new MultiDim(this.unitShape);
        double[][][] dloss_dwxb=(double[][][])dloss_dwxb_md.getData();
        int[][][] binomial= (int[][][])Binomial.binomialOfInt(keepProb,this.unitShape).getData();//二项分布

        //计算（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
        for(int x=0;x<dloss_dwxb.length;x++){
            for(int y=0;y<dloss_dwxb[x].length;y++){
                for(int z=0;z<dloss_dwxb[x][y].length;z++){
                    dloss_dwxb[x][y][z]=prtGradVal[x][y][z]*outs[x][y][z].prtGrad(wxb[x][y][z],targetVal);//（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
                }
            }
        }

//        input=ConvUtil.expand(input,inputShapeExpand);

        for(int w=0;w<diffW.length;w++){
            //计算卷积
//            double[][] conv=ConvUtil.conv((double[][][])input.getData(),dloss_dwxb,1,keepProb,strides);
//            diffW[w]=conv;

            //计算w的更新梯度
            //累计参数w的更新值
//            diffW[w][x][y][z]+=dloss_dwxb[x][y][z]*inputVal[x][y][z]*binomial[x][y][z];

            //累计参数b的更新值
//            diffB[w]+=dloss_dwxb[i]*binomial[i];
        }
//
//        //累计输入参数的更新值
//        if(currentPrtGrad!=null){
//            double[] currentPrtGradVal=(double[])currentPrtGrad.getData();
//
//            //计算输入值的更新梯度
//            double[] cpt=Linalg.inner(w,dloss_dwxb,true);
//            for(int i=0;i<currentPrtGradVal.length;i++){
//                currentPrtGradVal[i]+=cpt[i];
//            }
//        }
    }

    @Override
    public void flushBackUpdateParamPrtGrad(double rate) {
//        for(int i=0;i<this.outs.length;i++){
//            //计算w的更新梯度
//            for(int j=0;j<diffW[i].length;j++){
//                w[i][j]-=rate*diffW[i][j];
//            }
//            //计算b的更新梯度
//            b[i]-=rate*diffB[i];
//        }
    }

}
