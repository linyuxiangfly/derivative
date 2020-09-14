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
    void setLoss(Loss loss);

    /**
     *
     * @param x
     * @param y
     * @param batchSize
     */
    void evaluate(double[] x, double[] y, int batchSize);

    void predict(double x, int batchSize);

    void fit();
}
