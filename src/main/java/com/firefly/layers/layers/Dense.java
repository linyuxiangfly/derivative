package com.firefly.layers.layers;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.derivative.core.OperationUnary;
import com.firefly.derivative.operation.Var;
import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Neuron;
import com.firefly.layers.neuron.GeneralNeuron;
import com.firefly.math.Linalg;

/**
 * 全连接层
 */
public class Dense implements Layer {
    private int inputs;
    private int units;
    private Class<? extends OperationActivation> activationCls;

    private double[][] w;
    private double[] b;
    private double[][] diffW;
    private double[] diffB;

    private Var[] wxb;
    private OperationActivation[] outs;

    public Dense(){

    }

    public Dense(int units,Class<? extends OperationActivation> activationCls){
        this.units=units;
        this.activationCls=activationCls;
    }

    public Dense(int inputs,int units,Class<? extends OperationActivation> activationCls){
        this.inputs=inputs;
        this.units=units;
        this.activationCls=activationCls;
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

    @Override
    public void init() {
        w=new double[units][inputs];
        b=new double[units];

        diffW=new double[units][inputs];
        diffB=new double[units];

        //初始化神经元函数
        initFunc();

        //随机初始化参数
        randomInitParmas();
    }

    private void randomInitParmas(){
        for(int i=0;i<this.outs.length;i++){
            for(int j=0;j<this.w[i].length;j++){
                this.w[i][j]=Math.random();
            }
            this.b[i]=Math.random();
        }
    }

    private void initFunc(){
        outs=new OperationActivation[units];//输出单元数
        wxb=new Var[units];
        for(int i=0;i<units;i++){
            try {
                wxb[i]=new Var();
                outs[i]=activationCls.newInstance();
                outs[i].setVal(wxb[i]);
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

    @Override
    public void backUpdateInputPrtGrad(double[] prtGrad, double[] input, double[] outPrtGrad) {
//        for(int i=0;i<this.outs.length;i++){
//            double dloss_dwxb=prtGrad[i]*outs[i].prtGrad(wxb[i]);//（损失函数/激活函数）*（激活函数/wx+b）的偏导梯度
//
//            //累计输入参数的更新值
//            if(outPrtGrad!=null){
//                //计算x的更新梯度
//                for(int j=0;j<input.length;j++){
//                    outPrtGrad[i][j]+=dloss_dwxb*w[i][j];
//                }
//            }
//        }
    }

}
