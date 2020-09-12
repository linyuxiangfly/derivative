package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;

/**
 * 常量
 */
public class Const implements Function {
    double val;

    public Const(){

    }

    public Const(double val){
        this.val=val;
    }

    @Override
    public boolean isDx(Function dx) {
        return this==dx;
    }

    @Override
    public double prtGrad(Function dx) {
        return isDx(dx)?
                1:
                0;
    }

    @Override
    public double calc() {
        return val;
    }
}
