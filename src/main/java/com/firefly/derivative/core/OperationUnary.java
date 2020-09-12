package com.firefly.derivative.core;

import com.firefly.derivative.operation.Const;

/**
 * 单目运算符
 */
public class OperationUnary implements Function {
    private Function val;

    public OperationUnary(){

    }

    public OperationUnary(double val){
        this.val=new Const(val);
    }

    public OperationUnary(Function val){
        this.val=val;
    }

    public Function getVal() {
        return val;
    }

    public void setVal(Function val) {
        this.val = val;
    }

    public void setVal(double val) {
        this.val = new Const(val);
    }

    private Function oldDx;
    private boolean oldIsDx;
    @Override
    public boolean isDx(Function dx) {
        if(oldDx!=dx){
            oldDx=dx;
            oldIsDx=this==dx||val.isDx(dx);
        }
        return oldIsDx;
    }

    protected double prtGradEx(Function dx,double d){
//        return d*(val.isDx(dx)?val.prtGrad(dx):1);
        return d*val.prtGrad(dx);
    }

    @Override
    public double prtGrad(Function dx) {
        return 0;
    }

    @Override
    public double calc() {
        return 0;
    }
}
