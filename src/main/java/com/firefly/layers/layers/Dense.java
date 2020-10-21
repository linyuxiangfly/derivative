package com.firefly.layers.layers;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.derivative.operation.Var;
import com.firefly.layers.core.Layer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.init.params.InitParamsRandomOrdinary;
import com.firefly.layers.listeners.InitActivationListener;
import com.firefly.layers.listeners.InitParamsListener;
import com.firefly.math.Binomial;
import com.firefly.math.Linalg;

/**
 * 全连接层
 */
public class Dense implements Layer {
    private Shape inputShape;
    private Shape unitShape;
    private int inputs;//输出单元数
    private int units;//输出单元数

    private InitActivationListener initActivationListener;//初始化激活函数事件

    private InitParamsListener initParamsListener;//初始化参数事件

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

    public Dense(int units,InitActivationListener initActivationListener){
        this(0,units,initActivationListener,null);
    }

    public Dense(int inputs,int units,InitActivationListener initActivationListener){
        this(inputs,units,initActivationListener,null);
    }

    public Dense(int units,InitActivationListener initActivationListener, InitParamsListener initParamsListener){
        this(0,units,initActivationListener,initParamsListener);
    }

    public Dense(int inputs, int units, InitActivationListener initActivationListener, InitParamsListener initParamsListener){
        this.inputs=inputs;
        this.units=units;
        this.inputShape=new Shape(new int[]{inputs});
        this.unitShape=new Shape(new int[]{units});

        this.initActivationListener=initActivationListener;
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
                outs[i]=initActivationListener.newActivation();
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
        double[] inputVal=(double[])input.getData();

        double val=0;

        //计算wxb
        for(int i=0;i<this.outs.length;i++){
            //sigmoid(wx+b)
            val=Linalg.inner(w[i],inputVal)+b[i];

            wxb[i].setVal(val);
        }

        //计算 激活函数（wxb）
        for(int i=0;i<this.outs.length;i++){
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
    public void addBackUpdateParamPrtGrad(MultiDim input,MultiDim targetVal,MultiDim outFrontLayerPrtGrad,MultiDim backLayerPrtGrad) {
        double[] inputVal=(double[])input.getData();
        double[] backLayerPrtGradVal=(double[])backLayerPrtGrad.getData();

        double[] dloss_dwxb=new double[outs.length];

        for(int i=0;i<this.outs.length;i++){
            if(backLayerPrtGradVal[i]!=0){
                dloss_dwxb[i]=backLayerPrtGradVal[i]*outs[i].prtGrad(wxb[i],targetVal);//（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
            }

            if(dloss_dwxb[i]!=0){
                //计算w的更新梯度
                for(int j=0;j<diffW[i].length;j++){
                    //累计参数w的更新值
                    diffW[i][j]+=dloss_dwxb[i]*inputVal[j];
                }

                //累计参数b的更新值
                diffB[i]+=dloss_dwxb[i];
            }
        }

        //累计输入参数的更新值
        if(outFrontLayerPrtGrad!=null){
            double[] outFrontLayerPrtGradVal=(double[])outFrontLayerPrtGrad.getData();

            //计算输入值的更新梯度
            double[] cpt=Linalg.inner(w,dloss_dwxb,true);
            for(int i=0;i<outFrontLayerPrtGradVal.length;i++){
                outFrontLayerPrtGradVal[i]+=cpt[i];
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
