package com.firefly.layers.neuron;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationUnary;
import com.firefly.derivative.operation.AddMult;
import com.firefly.derivative.operation.Mcl;
import com.firefly.derivative.operation.Var;
import com.firefly.layers.core.Neuron;
import com.firefly.math.Linalg;

/**
 * 一般的神经元
 * y=wx+b
 */
public class GeneralNeuron implements Neuron {
    private int inputs;
    private Class<? extends OperationUnary> activationCls;//激活函数

    private double[] w;
    private double b;
    private Var wxb=new Var();
    private Function activation;

    private double[] diffW;
    private double diffB;

    public GeneralNeuron(){

    }

    public GeneralNeuron(int inputs,Class<? extends OperationUnary> activationCls) {
        this.inputs=inputs;
        this.activationCls=activationCls;
    }

    private Function createFunc(Class<? extends OperationUnary> activationCls, Function wxb) throws IllegalAccessException, InstantiationException {
        //创建激活函数，以及设置值
        OperationUnary ret=activationCls.newInstance();
        ret.setVal(wxb);
        return ret;
    }

    public double[] getW() {
        return w;
    }

    public void setW(double[] w) {
        this.w = w;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public Class<? extends OperationUnary> getActivationCls() {
        return activationCls;
    }

    public void setActivationCls(Class<? extends OperationUnary> activationCls) {
        this.activationCls = activationCls;
    }

    @Override
    public void init() throws InstantiationException, IllegalAccessException {
        activation=createFunc(activationCls,wxb);
        if(this.w==null){
            this.w=new double[inputs];
            for(int i=0;i<this.w.length;i++){
                this.w[i]=Math.random();
            }
            this.diffW=new double[inputs];
        }
    }

    @Override
    public double calc(double[] input) {
        //wx+b
        wxb.setVal(Linalg.inner(w,input)+b);
        return activation.calc();
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {
        //重置w的更新梯度
        for(int j=0;j<diffW.length;j++){
            diffW[j]=0;
        }
        //计算b的更新梯度
        diffB=0;
    }

    @Override
    public void addBackUpdateParamPrtGrad(double[] prtGrad,double[] input,double[] currentPrtGrad) {
        double dy_dwxb=activation.prtGrad(wxb);//激活函数与wx+b的偏导梯度

        //计算w的更新梯度
        for(int j=0;j<diffW.length;j++){
            double dw=0;
            for(int i=0;i<prtGrad.length;i++){
                dw+=prtGrad[i]*dy_dwxb*input[j];
            }
            //累计参数w的更新值
            diffW[j]+=dw;
        }

        if(currentPrtGrad!=null){
            //计算x的更新梯度
            for(int j=0;j<input.length;j++){
                for(int i=0;i<prtGrad.length;i++){
                    currentPrtGrad[j]+=prtGrad[i]*dy_dwxb*w[j];
                }
            }
        }

        //计算b的更新梯度
        double db=0;
        for(int i=0;i<prtGrad.length;i++){
            db+=prtGrad[i]*dy_dwxb;
        }
        //累计参数b的更新值
        diffB+=db;
    }

    @Override
    public void flushBackUpdateParamPrtGrad(double rate) {
        //计算w的更新梯度
        for(int j=0;j<diffW.length;j++){
            w[j]-=rate*diffW[j];
        }
        //计算b的更新梯度
        b-=rate*diffB;
    }

    @Override
    public void backUpdateInputPrtGrad(double[] prtGrad,double[] input,double[] outPrtGrad) {
        double dy_dwxb=activation.prtGrad(wxb);//激活函数与wx+b的偏导梯度

        //计算w的更新梯度
        for(int j=0;j<input.length;j++){
            double diffW=0;
            for(int i=0;i<prtGrad.length;i++){
                diffW+=prtGrad[i]*dy_dwxb*w[j];
            }
            outPrtGrad[j]+=diffW;
        }
    }
}
