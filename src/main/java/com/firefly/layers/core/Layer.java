package com.firefly.layers.core;

/**
 * 网络层
 */
public interface Layer extends java.io.Serializable{
    /**
     * 获取层的输入数量
     * @return
     */
    int getInputs();

    /**
     * 设置层的输入数量
     * @param inputs
     */
    void setInputs(int inputs);

    /**
     * 获取层的输出数量，即是神经元数
     * @return
     */
    int getUnits();

    /**
     * 设置层的输出数量，即是神经元数
     * @param units
     */
    void setUnits(int units);

    /**
     * 获取层的W参数
     * @return
     */
    double[][] getW();

    /**
     * 设置层的W参数
     * @param w
     */
    void setW(double[][] w);

    /**
     * 获取层的B参数
     * @return
     */
    double[] getB();

    /**
     * 设置层的B参数
     * @param b
     */
    void setB(double[] b);

    /**
     * 初始化层
     */
    void init();

    /**
     * 正向计算
     * @param input 输入数量
     * @param out 计算后并输出数据
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
