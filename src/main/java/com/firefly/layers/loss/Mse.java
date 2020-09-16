package com.firefly.layers.loss;

import com.firefly.derivative.core.Function;
import com.firefly.layers.core.Loss;
import com.firefly.math.Linalg;

/**
 * 均方误差（MSE，mean squared error）
 */
public class Mse implements Loss {
    private int inputs;
    private int units=1;

    public int getInputs() {
        return inputs;
    }

    public void setInputs(int inputs) {
        this.inputs = inputs;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
//        this.units = units;
    }

    @Override
    public void init() {

    }

    @Override
    public void calc(double[] input,double[] targetVal,double[] out) {
        double[] error=new double[input.length];
        for(int i=0;i<input.length;i++){
            error[i]=targetVal[i]-input[i];
        }
        out[0]=Linalg.inner(error, error)*0.5;
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {

    }

    @Override
    public void addBackUpdateParamPrtGrad(double[] prtGrad, double[] input,double[] currentPrtGrad) {

    }

    @Override
    public void flushBackUpdateParamPrtGrad(double rate) {

    }

    @Override
    public void backUpdateInputPrtGrad(double[] prtGrad, double[] input, double[] outPrtGrad) {

    }

    @Override
    public double[] prtGrad(double[] input,double[] targetVal) {
        return Linalg.sub(input,targetVal);
    }
}
