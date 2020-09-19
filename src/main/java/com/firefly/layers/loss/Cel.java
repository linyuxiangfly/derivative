package com.firefly.layers.loss;

import com.firefly.derivative.util.MathEx;
import com.firefly.layers.core.Loss;
import com.firefly.math.Linalg;
import com.firefly.math.Statistics;

/**
 * 交叉熵损失函数
 */
public class Cel implements Loss {
    @Override
    public void calc(double[] input,double[] targetVal,double[] out) {
        double ret=0;
//        double si=sumInput(input);//累计所有输入的和
        for(int i=0;i<input.length;i++){
            ret+=targetVal[i]* Math.log(input[i]);
        }
        out[0]=-ret;
    }

    /**
     * 累计所有输入的值
     * @param input
     * @return
     */
    private double sumInput(double[] input){
        return Statistics.sum(input);
    }

    @Override
    public double[] prtGrad(double[] input,double[] targetVal) {
        double[] ret=new double[input.length];
        for(int i=0;i<input.length;i++){
            ret[i]=-targetVal[i]/input[i];
        }
        return ret;
    }
}
