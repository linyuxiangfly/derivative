package com.firefly.layers.core;

/**
 * 神经元
 */
public interface Neuron {
    /**
     * 初始化神经元
     */
    void init() throws InstantiationException, IllegalAccessException;

    /**
     * 正向计算
     * @param input
     */
    double calc(double[] input);

    /**
     * 重置反向更新参数梯度
     */
    void resetBackUpdateParamPrtGrad();

    /**
     * 累加反向更新参数梯度
     * @param prtGrad 下一层的梯度
     * @param input 输入值
     */
    void addBackUpdateParamPrtGrad(double[] prtGrad,double[] input,double[] currentPrtGrad);

    /**
     * 更新参数梯度
     * @param rate 更新比例
     */
    void flushBackUpdateParamPrtGrad(double rate);

    /**
     * 反向更新输入变量梯度
     * @param prtGrad 下一层的梯度
     * @param input 输入值
     * @param outPrtGrad 输出梯度值
     */
    void backUpdateInputPrtGrad(double[] prtGrad,double[] input,double[] outPrtGrad);
}
