package com.firefly.layers.core;

import com.firefly.layers.listeners.FitControl;
import com.firefly.layers.listeners.LossCallBackListener;

import java.util.List;

/**
 * 模块
 */
public interface Model extends java.io.Serializable{
    /**
     * 添加层
     * @param layer 层对象
     */
    void add(Layer layer);

    /**
     * 移除层
     * @param layer
     */
    void remove(Layer layer);

    /**
     * 获取所有层
     * @return
     */
    List<Layer> getLayers();

    /**
     * 编译
     * @param lossCls 损失函数
     */
    void setLossCls(Class<? extends Loss> lossCls);

    /**
     * 初始化
     */
    void init();

    /**
     *
     * @param x
     * @param y
     * @param batchSize
     */
    void evaluate(double[][] x, double[][] y, int batchSize);

    double[] predict(double[] x);

    void fit(double[][] x, double[][] y, int epoch, int batchSize);

    void fit(double[][] x, double[][] y, int epoch, int batchSize, LossCallBackListener lossCallBackListener);

    void fit(double[][] x, double[][] y, int epoch, int batchSize, LossCallBackListener lossCallBackListener, FitControl fitControl);
}
