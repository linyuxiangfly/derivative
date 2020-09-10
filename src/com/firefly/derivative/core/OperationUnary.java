package com.firefly.derivative.core;

/**
 * 单目运算符
 */
public class OperationUnary implements Function {
    private Function val;

    public OperationUnary(){

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

    protected double derEx(Function dx,double d){
        return d*(val.isDx(dx)?val.der(dx):1);
    }

    @Override
    public double der(Function dx) {
        return 0;
    }

    @Override
    public double calc() {
        return 0;
    }
}
