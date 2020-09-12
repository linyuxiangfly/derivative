package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;

/**
 * 变量
 */
public class Var implements Function {
    double val;

    public Var(){

    }

    public Var(double val){
        this.val=val;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    @Override
    public double prtGrad(Function dx) {
        return isDx(dx)?
                1:
                0;
    }

    @Override
    public boolean isDx(Function dx) {
        return this==dx;
    }

    @Override
    public double calc() {
        return val;
    }
}
