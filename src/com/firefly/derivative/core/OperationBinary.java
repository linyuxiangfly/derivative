package com.firefly.derivative.core;

/**
 * 双目运算符
 */
public class OperationBinary implements Function {
    private Function a,b;

    public OperationBinary(){

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

    public Function getB() {
        return b;
    }

    public void setB(Function b) {
        this.b = b;
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

    protected double derEx(Function dx,double d){
        return d*(a.isDx(dx)?a.der(dx):1)
                *(b.isDx(dx)?b.der(dx):1);
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
