package com.firefly.layers.core;

import com.firefly.derivative.core.Function;

/**
 * 神经元
 */
public interface Neuron extends Function {
    /**
     * 初始化神经元
     */
    void init() throws InstantiationException, IllegalAccessException;

    /**
     * 反向更新梯度
     */
    void backUpdatePrtGrad(double[] prtGrad);
}
