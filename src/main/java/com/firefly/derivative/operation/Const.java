package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;

/**
 * 常量
 */
public class Const implements Function {
    private static final long serialVersionUID = 1L;

    double val;

    public Const(double val){
        this.val=val;
    }

    public double getVal() {
        return val;
    }

    @Override
    public boolean isDx(Function dx) {
        return this==dx;
    }

    @Override
    public double prtGrad(Function dx) {
        return 0;
    }

    @Override
    public double calc() {
        return val;
    }
}
