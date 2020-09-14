package com.firefly.layers.core;

/**
 * 模块
 */
public interface Model {
    /**
     * 添加层
     * @param layer 层对象
     */
    void add(Layer layer);

    /**
     * 编译
     * @param loss 损失函数
     */
    void compile(Loss loss);
}
