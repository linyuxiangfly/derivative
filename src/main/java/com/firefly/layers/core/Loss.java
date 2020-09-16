package com.firefly.layers.core;

import com.firefly.derivative.core.Function;

/**
 * 损失函数
 */
public interface Loss{
    /**
     * 偏梯度
     * @param input 输入参数
     * @param targetVal 目标值
     * @return
     */
    double[] prtGrad(double[] input,double[] targetVal);

    /**
     * 正向计算
     * @param input
     */
    void calc(double[] input,double[] targetVal,double[] out);
}
