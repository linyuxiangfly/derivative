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
     * @param lossCls 损失函数
     */
    void setLossCls(Class<? extends Loss> lossCls);

    /**
     *
     * @param x
     * @param y
     * @param batchSize
     */
    void evaluate(double[][] x, double[][] y, int batchSize);

    double[] predict(double[] x);

    void fit(double[][] x, double[][] y, int epoch, int batchSize);
}
