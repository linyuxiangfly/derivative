package com.firefly.layers.core;

import com.firefly.layers.data.MultiDim;

/**
 * 优化器
 */
public interface Optimizer extends java.io.Serializable {
    /**
     * 更新参数梯度
     * @param params 需要更新的参数
     * @param prtGrad 梯度
     */
    void update(MultiDim params,MultiDim prtGrad);
}
