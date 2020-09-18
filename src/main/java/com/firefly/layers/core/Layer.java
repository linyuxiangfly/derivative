package com.firefly.layers.core;

import com.firefly.derivative.core.Function;

/**
 * 网络层
 */
public interface Layer {
    int getInputs();

    void setInputs(int inputs);

    int getUnits();

    void setUnits(int units);

    void init();

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
     * @param currentPrtGrad 识差函数/当前层的输入的梯度
     */
    void addBackUpdateParamPrtGrad(double[] prtGrad,double[] input,double[] currentPrtGrad);

    /**
     * 更新参数梯度
     * @param rate 更新比例
     */
    void flushBackUpdateParamPrtGrad(double rate);

}
