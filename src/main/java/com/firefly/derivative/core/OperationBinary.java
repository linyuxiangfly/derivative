package com.firefly.derivative.core;

import com.firefly.derivative.operation.Const;

/**
 * 双目运算符
 */
public class OperationBinary implements Function {
    private static final long serialVersionUID = 1L;

    private Function a,b;

    public OperationBinary(){

    }

    public OperationBinary(double a, double b){
        this.a=new Const(a);
        this.b=new Const(b);
    }

    public OperationBinary(Function a, double b){
        this.a=a;
        this.b=new Const(b);
    }

    public OperationBinary(double a, Function b){
        this.a=new Const(a);
        this.b=b;
    }

    public OperationBinary(Function a, Function b){
        this.a=a;
        this.b=b;
    }

    public Function getA() {
        return a;
    }

    public void setA(Function a) {
        this.a = a;
    }

    public void setA(double a) {
        this.a = new Const(a);
    }

    public Function getB() {
        return b;
    }

    public void setB(Function b) {
        this.b = b;
    }

    public void setB(double b) {
        this.b = new Const(b);
    }

    private Function oldDx;
    private boolean oldIsDx;
    @Override
    public boolean isDx(Function dx) {
        if(oldDx!=dx){
            oldDx=dx;
            oldIsDx=this==dx||a.isDx(dx)||b.isDx(dx);
        }
        return oldIsDx;
    }

    private double prtGradEx(Function param,Function dx,double d){
//        return d*(param.isDx(dx)?param.prtGrad(dx):1);
        return d*param.prtGrad(dx);
    }

    protected double prtGradExA(Function dx,double d){
        return prtGradEx(a,dx,d);
    }

    protected double prtGradExB(Function dx,double d){
        return prtGradEx(b,dx,d);

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
