package com.firefly.layers.core;

import com.firefly.layers.listeners.FitControl;
import com.firefly.layers.listeners.LossCallBackListener;

import java.util.List;

/**
 * 模型
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
     * 预测
     * @param x 输入数据进行预测
     * @return 返回预测结果
     */
    double[] predict(double[] x);

    /**
     *
     * @param x 需要训练的数据
     * @param y 训练数据的标签
     * @param epoch 训练次数
     * @param batchSize 训练数据批量大小
     */
    void fit(double[][] x, double[][] y, int epoch, int batchSize);

    /**
     *
     * @param x 需要训练的数据
     * @param y 训练数据的标签
     * @param epoch 训练次数
     * @param batchSize 训练数据批量大小
     * @param lossCallBackListener 损失函数返回事件
     */
    void fit(double[][] x, double[][] y, int epoch, int batchSize, LossCallBackListener lossCallBackListener);

    /**
     *
     * @param x 需要训练的数据
     * @param y 训练数据的标签
     * @param epoch 训练次数
     * @param batchSize 训练数据批量大小
     * @param lossCallBackListener 损失函数返回事件
     * @param fitControl 拟合过程控制，可以设置达到某个条件退出训练
     */
    void fit(double[][] x, double[][] y, int epoch, int batchSize, LossCallBackListener lossCallBackListener, FitControl fitControl);
}
