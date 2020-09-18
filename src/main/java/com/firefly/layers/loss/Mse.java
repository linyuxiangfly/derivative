package com.firefly.layers.loss;

import com.firefly.derivative.core.Function;
import com.firefly.layers.core.Loss;
import com.firefly.math.Linalg;

/**
 * 均方误差（MSE，mean squared error）
 */
public class Mse implements Loss {
    @Override
    public void calc(double[] input,double[] targetVal,double[] out) {
        double[] error=new double[input.length];
        for(int i=0;i<input.length;i++){
            error[i]=targetVal[i]-input[i];
        }
        out[0]=Linalg.inner(error, error)*0.5;
    }

    @Override
    public double[] prtGrad(double[] input,double[] targetVal) {
        return Linalg.sub(input,targetVal);
    }
}
