package com.firefly.layers.core;

import com.firefly.derivative.core.Function;

/**
 * 网络层
 */
public interface Layer {
    /**
     * 拟合
     * @param x
     * @param y
     */
    void fit(Function[] x,Function y);
}
