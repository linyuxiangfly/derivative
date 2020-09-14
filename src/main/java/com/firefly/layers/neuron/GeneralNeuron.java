package com.firefly.layers.neuron;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationUnary;
import com.firefly.derivative.operation.Add;
import com.firefly.derivative.operation.AddMult;
import com.firefly.derivative.operation.Mcl;
import com.firefly.derivative.operation.Var;
import com.firefly.layers.core.Neuron;

/**
 * 一般的神经元
 * y=wx+b
 */
public class GeneralNeuron implements Neuron {
    private double[] x;
    private double[] w;
    private double b;
    private Var wxb=new Var();
    private Class<? extends OperationUnary> activationCls;//激活函数

    private Function func;

    public GeneralNeuron(){

    }

    public GeneralNeuron(double[] w,double b,Class<? extends OperationUnary> activationCls) {
        this.w=w;
        this.b=b;
        this.activationCls=activationCls;
    }

    /**
     * 创建w*x以及b数组
     * @param x
     * @param w
     * @return
     */
    private Function[] createWxb(Function[] x,Function[] w,Function b){
        Function[] ret=new Function[x.length+1];
        for(int i=0;i<x.length-1;i++){
            ret[i]=new Mcl(x[i],w[i]);//w*x
        }
        ret[x.length-1]=b;
        return ret;
    }

    /**
     * 创建神经元函数
     * @param activationCls 激活函数
     * @param wxb
     * @return
     */
    private Function createFunc(Class<? extends OperationUnary> activationCls, Function[] wxb) throws IllegalAccessException, InstantiationException {
        Function add=new AddMult(wxb);//所有元素相加
        //创建激活函数，以及设置值
        OperationUnary ret=activationCls.newInstance();
        ret.setVal(add);
        return ret;
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

    /**
     * 如果参数中是否至少有一个要求偏导
     * @param params
     * @param dx
     * @return
     */
    private boolean isDxParams(Function[] params,Function dx){
        if(params!=null && params.length>0){
            for(Function func:params){
                if(func.isDx(dx)){
                    return true;
                }
            }
        }
        return false;
    }

    private Function oldDx;
    private boolean oldIsDx;
    @Override
    public boolean isDx(Function dx) {
        if(oldDx!=dx){
            oldDx=dx;
            oldIsDx=this==dx;
        }
        return oldIsDx;
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;
//        if(this==dx){
//            val=1;
//        }else{
//            if(this.getParams()!=null && this.getParams().length>0){
//                for(Function param:this.getParams()){
//                    if(param.isDx(dx)){
//                        val+=this.prtGradEx(param,dx,1.0);
//                    }
//                }
//            }
//        }

        return val;
    }

    @Override
    public double calc() {
        if(func!=null){
            return func.calc();
        }
        return 0;
    }

    @Override
    public void init() throws InstantiationException, IllegalAccessException {
        func=createFunc(activationCls,wxb);
    }

    @Override
    public void backUpdatePrtGrad(double[] prtGrad) {
        //todo
    }
}
