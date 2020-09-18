package com.firefly.layers.layers;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.derivative.operation.Var;
import com.firefly.layers.core.Layer;
import com.firefly.layers.init.params.InitParamsRandomOrdinary;
import com.firefly.layers.listeners.InitParamsListener;
import com.firefly.math.Linalg;

/**
 * 全连接层
 */
public class Dense implements Layer {
    private int inputs;//输出单元数
    private int units;//输出单元数
    private Class<? extends OperationActivation> activationCls;//激活函数类
    private Function[] activationSettings;
    private InitParamsListener initParamsListener;//初始化参数事件

    private double[][] w;
    private double[] b;
    private double[][] diffW;
    private double[] diffB;

    private Var[] wxb;
    private OperationActivation[] outs;

    public Dense(){

    }

    public Dense(int units, Class<? extends OperationActivation> activationCls){
        this(units,activationCls,null);
    }

    public Dense(int units, Class<? extends OperationActivation> activationCls, InitParamsListener initParamsListener){
        this(0,units,activationCls,initParamsListener);
    }

    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls){
        this(inputs,units,activationCls,(Function[])null);
    }


    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls, Function[] activationSettings){
        this(inputs,units,activationCls,activationSettings,null);
    }

    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls, InitParamsListener initParamsListener){
        this(inputs,units,activationCls,null,initParamsListener);
    }

    public Dense(int inputs, int units, Class<? extends OperationActivation> activationCls, Function[] activationSettings, InitParamsListener initParamsListener){
        this.inputs=inputs;
        this.units=units;
        this.activationCls=activationCls;
        this.activationSettings=activationSettings;
        this.initParamsListener=initParamsListener;
    }

    public int getInputs() {
        return inputs;
    }

    public void setInputs(int inputs) {
        this.inputs = inputs;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double[][] getW() {
        return w;
    }

    public void setW(double[][] w) {
        this.w = w;
    }

    public double[] getB() {
        return b;
    }

    public void setB(double[] b) {
        this.b = b;
    }

    @Override
    public void init() {
        w=new double[units][inputs];
        b=new double[units];

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
        initParamsListener.paramWSize(this.units,this.inputs);
        initParamsListener.paramBSize(this.units);

        for(int i=0;i<this.units;i++){
            for(int j=0;j<this.inputs;j++){
                this.w[i][j]=initParamsListener.initParamW(i,j);
            }
            this.b[i]=initParamsListener.initParamB(i);
        }
    }

    private void initFunc(){
        outs=new OperationActivation[units];//输出单元数
        wxb=new Var[units];
        for(int i=0;i<units;i++){
            try {
                wxb[i]=new Var();
                outs[i]=activationCls.newInstance();
                //激活函数设置值
                outs[i].setSettings(activationSettings);
                //设置当前数据
                outs[i].setVal(wxb[i]);
                //设置相关的数据
                outs[i].setRelations(wxb);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public void calc(double[] input,double[] out) {
        for(int i=0;i<this.outs.length;i++){
            //sigmoid(wx+b)
            wxb[i].setVal(Linalg.inner(w[i],input)+b[i]);
            out[i]=this.outs[i].calc();
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
    public void addBackUpdateParamPrtGrad(double[] prtGrad, double[] input,double[] currentPrtGrad) {
        double[] dloss_dwxb=new double[outs.length];
        for(int i=0;i<this.outs.length;i++){
            dloss_dwxb[i]=prtGrad[i]*outs[i].prtGrad(wxb[i]);//（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度

            //计算w的更新梯度
            for(int j=0;j<diffW[i].length;j++){
                //累计参数w的更新值
                diffW[i][j]+=dloss_dwxb[i]*input[j];
            }

            //累计参数b的更新值
            diffB[i]+=dloss_dwxb[i];
        }

        //累计输入参数的更新值
        if(currentPrtGrad!=null){
            //计算输入值的更新梯度
            double[] cpt=Linalg.inner(w,dloss_dwxb,true);
            for(int i=0;i<currentPrtGrad.length;i++){
                currentPrtGrad[i]+=cpt[i];
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
