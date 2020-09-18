package com.firefly.derivative.core;

/**
 * 函数
 */
public interface Function extends java.io.Serializable{
    boolean isDx(Function dx);

    /**
     * 偏梯度
     * @param dx 求该函数与dx之间的梯度
     * @return
     */
    double prtGrad(Function dx);

    /**
     * 计算该函数的结果
     * @return
     */
    double calc();
}
