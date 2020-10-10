package com.firefly.layers.layers;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.derivative.operation.Var;
import com.firefly.layers.core.Layer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.init.params.InitParamsRandomOrdinary;
import com.firefly.layers.listeners.InitParamsListener;
import com.firefly.math.Binomial;
import com.firefly.math.Linalg;

/**
 * 全连接层
 */
public class Dense implements Layer {
    private static float KEEP_PROB_DEFAULT=1.0F;

    private Shape inputShape;
    private Shape unitShape;
    private int inputs;//输出单元数
    private int units;//输出单元数

    private Class<? extends OperationActivation> activationCls;//激活函数类

    private MultiDim activationSettingsMd;
    private Function[] activationSettings;
    private InitParamsListener initParamsListener;//初始化参数事件
    private float keepProb=KEEP_PROB_DEFAULT;//节点保留概率，dropout功能

    private MultiDim wmd;
    private MultiDim bmd;
    private double[][] w;
    private double[] b;
    private double[][] diffW;
    private double[] diffB;

    private MultiDim wxbmd;
    private Var[] wxb;
    private OperationActivation[] outs;

    public Dense(){

    }

    public Dense(int units, Class<? extends OperationActivation> activationCls){
        this(units,activationCls,KEEP_PROB_DEFAULT);
    }

    public Dense(int units, Class<? extends OperationActivation> activationCls,float keepProb){
        this(units,activationCls,keepProb,(InitParamsListener)null);
    }

    public Dense(int units, Class<? extends OperationActivation> activationCls, InitParamsListener initParamsListener){
        this(0,units,activationCls,KEEP_PROB_DEFAULT,initParamsListener);
    }

    public Dense(int units, Class<? extends OperationActivation> activationCls,float keepProb, InitParamsListener initParamsListener){
        this(0,units,activationCls,keepProb,initParamsListener);
    }

    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls){
        this(inputs,units,activationCls,KEEP_PROB_DEFAULT,(Function[])null);
    }

    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls,float keepProb){
        this(inputs,units,activationCls,keepProb,(Function[])null);
    }

    public Dense(int units,Class<? extends OperationActivation> activationCls, Function[] activationSettings){
        this(0,units,activationCls,KEEP_PROB_DEFAULT,activationSettings,null);
    }

    public Dense(int units,Class<? extends OperationActivation> activationCls,float keepProb, Function[] activationSettings){
        this(0,units,activationCls,keepProb,activationSettings,null);
    }

    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls, Function[] activationSettings){
        this(inputs,units,activationCls,KEEP_PROB_DEFAULT,activationSettings,null);
    }

    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls,float keepProb, Function[] activationSettings){
        this(inputs,units,activationCls,keepProb,activationSettings,null);
    }

    public Dense(int units,Class<? extends OperationActivation> activationCls,Function[] activationSettings, InitParamsListener initParamsListener){
        this(0,units,activationCls,KEEP_PROB_DEFAULT,activationSettings,initParamsListener);
    }

    public Dense(int units,Class<? extends OperationActivation> activationCls,float keepProb,Function[] activationSettings, InitParamsListener initParamsListener){
        this(0,units,activationCls,keepProb,activationSettings,initParamsListener);
    }

    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls,InitParamsListener initParamsListener){
        this(inputs,units,activationCls,KEEP_PROB_DEFAULT,null,initParamsListener);
    }

    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls,float keepProb, InitParamsListener initParamsListener){
        this(inputs,units,activationCls,keepProb,null,initParamsListener);
    }

    public Dense(int inputs, int units, Class<? extends OperationActivation> activationCls, Function[] activationSettings, InitParamsListener initParamsListener){
        this(inputs,units,activationCls,KEEP_PROB_DEFAULT,activationSettings,initParamsListener);
    }

    public Dense(int inputs, int units, Class<? extends OperationActivation> activationCls,float keepProb, Function[] activationSettings, InitParamsListener initParamsListener){
        this.inputs=inputs;
        this.units=units;
        this.inputShape=new Shape(new int[]{inputs});
        this.unitShape=new Shape(new int[]{units});

        this.activationCls=activationCls;
        this.keepProb=keepProb;

        this.activationSettings=activationSettings;
        if(activationSettings!=null){
            this.activationSettingsMd=new MultiDim(Function.class,new Shape(new int[]{activationSettings.length}),activationSettings);
        }

        this.initParamsListener=initParamsListener;
    }

    public Shape getInputShape() {
        return inputShape;
    }

    public void setInputShape(Shape inputShape) {
        inputs=inputShape.getNums();
//        this.inputShape = inputShape;
        this.inputShape = new Shape(new int[]{inputs});

//        if(inputShape.getDims().length==1){
//            this.inputs=inputShape.getDims()[0];
//        }else{
//            //只允许1维数组
//            throw new RuntimeException("Only 1-dimensional arrays are allowed");
//        }
    }

    public Shape getUnitShape() {
        return unitShape;
    }

    public void setUnitShape(Shape unitShape) {
        this.unitShape = unitShape;
        if(unitShape.getDims().length==1){
            this.units=unitShape.getDims()[0];
        }else{
            //只允许1维数组
            throw new RuntimeException("Only 1-dimensional arrays are allowed");
        }
    }

    public MultiDim getW() {
        return wmd;
    }

    public void setW(MultiDim w) {
        this.wmd = w;
        this.w=(double[][])w.getData();
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
        w=new double[units][inputs];
        b=new double[units];
        wmd=new MultiDim(Double.TYPE,new Shape(new int[]{units,inputs}),w);
        bmd=new MultiDim(Double.TYPE,new Shape(new int[]{units}),b);

        diffW=new double[units][inputs];
        diffB=new double[units];

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

        ShapeIndex wsi=new ShapeIndex(new int[]{0,0});
        ShapeIndex bsi=new ShapeIndex(new int[]{0});
        for(int i=0;i<this.units;i++){
            for(int j=0;j<this.inputs;j++){
                wsi.setDimIndexVal(0,i);
                wsi.setDimIndexVal(1,j);

                this.w[i][j]=initParamsListener.initParamW(wsi);
            }

            bsi.setDimIndexVal(0,i);
            this.b[i]=initParamsListener.initParamB(bsi);
        }
    }

    private void initFunc(){
        outs=new OperationActivation[units];//输出单元数
        wxb=new Var[units];
        wxbmd=new MultiDim(Var.class,new Shape(new int[]{wxb.length}),wxb);

        for(int i=0;i<units;i++){
            try {
                wxb[i]=new Var();
                outs[i]=activationCls.newInstance();
                //激活函数设置值
                outs[i].setSettings(activationSettingsMd);
                //设置当前数据
                outs[i].setVal(wxb[i]);
                //设置相关的数据
                outs[i].setRelations(wxbmd);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public void calc(MultiDim input,MultiDim out) {
        double[] outVal=(double[])out.getData();

        double val=0;

        for(int i=0;i<this.outs.length;i++){
            //sigmoid(wx+b)
            val=Linalg.inner(w[i],input)+b[i];
            val/=keepProb;//参数除以keep_prob来保证输出的期望值不变
            wxb[i].setVal(val);

            outVal[i]=this.outs[i].calc();
        }
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {
        for(int i=0;i<this.outs.length;i++){
            //重置w的更新梯度
            for(int j=0;j<diffW[i].length;j++){
                diffW[i][j]=0;
            }
            //计算b的更新梯度
            diffB[i]=0;
        }
    }

    @Override
    public void addBackUpdateParamPrtGrad(MultiDim input,MultiDim targetVal,MultiDim inputPrtGrad,MultiDim outPrtGrad) {
//        double[] inputVal=(double[])input.getData();
        double[] prtGradVal=(double[])outPrtGrad.getData();

        double[] dloss_dwxb=new double[outs.length];
        int[] binomial= Binomial.binomialOfInt(keepProb,this.outs.length);//二项分布

        for(int i=0;i<this.outs.length;i++){
            dloss_dwxb[i]=prtGradVal[i]*outs[i].prtGrad(wxb[i],targetVal);//（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度

            //计算w的更新梯度
            for(int j=0;j<diffW[i].length;j++){
                //累计参数w的更新值
                diffW[i][j]+=dloss_dwxb[i]*(double)input.getOneDim2MultDimIndexVal(j)*binomial[i];
            }

            //累计参数b的更新值
            diffB[i]+=dloss_dwxb[i]*binomial[i];
        }

        //累计输入参数的更新值
        if(inputPrtGrad!=null){
            double[] currentPrtGradVal=(double[])inputPrtGrad.getData();

            //计算输入值的更新梯度
            double[] cpt=Linalg.inner(w,dloss_dwxb,true);
            for(int i=0;i<currentPrtGradVal.length;i++){
                currentPrtGradVal[i]+=cpt[i];
            }
        }
    }

    @Override
    public void flushBackUpdateParamPrtGrad(double rate) {
        for(int i=0;i<this.outs.length;i++){
            //计算w的更新梯度
            for(int j=0;j<diffW[i].length;j++){
                w[i][j]-=rate*diffW[i][j];
            }
            //计算b的更新梯度
            b[i]-=rate*diffB[i];
        }
    }

}
