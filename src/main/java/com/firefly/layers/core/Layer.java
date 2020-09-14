package com.firefly.layers.core;

import com.firefly.derivative.core.Function;

/**
 * 网络层
 */
public interface Layer {
    /**
     * 正向计算
     * @param input
     */
    void calc(double[] input,double[] out);

    /**
     * 重置反向更新参数梯度
     */
    void resetBackUpdateParamPrtGrad();

    /**
     * 累加反向更新参数梯度
     * @param prtGrad 下一层的梯度
     * @param input 输入值
     */
    void addBackUpdateParamPrtGrad(double[] prtGrad,double[] input);

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
