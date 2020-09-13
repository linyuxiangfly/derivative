package com.firefly.layers.neuron;

import com.firefly.derivative.core.Function;
import com.firefly.layers.core.Neuron;

/**
 * 一般的神经元
 * y=wx+b
 */
public class GeneralNeuron implements Neuron {
    private Function[] x;
    private Function[] w;
    private Function b;
    private Function activation;//激活函数

    public GeneralNeuron(){

    }

    public GeneralNeuron(Function[] x,Function[] w,Function b,Function activation){
        this.x=x;
        this.w=w;
        this.b=b;
        this.activation=activation;
    }

    public Function[] getX() {
        return x;
    }

    public void setX(Function[] x) {
        this.x = x;
    }

    public Function[] getW() {
        return w;
    }

    public void setW(Function[] w) {
        this.w = w;
    }

    public Function getB() {
        return b;
    }

    public void setB(Function b) {
        this.b = b;
    }

    public Function getActivation() {
        return activation;
    }

    public void setActivation(Function activation) {
        this.activation = activation;
    }

    @Override
    public boolean isDx(Function dx) {
        return false;
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
