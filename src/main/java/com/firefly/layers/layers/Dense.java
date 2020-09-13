package com.firefly.layers.layers;

import com.firefly.derivative.core.Function;
import com.firefly.layers.core.Layer;

/**
 * 全连接层
 */
public class Dense implements Layer {
    private Function[] x;
    private Function[][] w;
    private Function[] b;

    public Dense(){

    }

    public Dense(Function[] x,Function[][] w,Function[] b){
        this.x=x;
        this.w=w;
        this.b=b;
    }

    public Function[] getX() {
        return x;
    }

    public void setX(Function[] x) {
        this.x = x;
    }

    public Function[][] getW() {
        return w;
    }

    public void setW(Function[][] w) {
        this.w = w;
    }

    public Function[] getB() {
        return b;
    }

    public void setB(Function[] b) {
        this.b = b;
    }

    @Override
    public void fit(Function[] x, Function y) {

    }
}
