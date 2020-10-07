package com.firefly.layers.core;

import com.firefly.layers.data.MultiDim;

/**
 * 损失函数
 */
public interface Loss extends java.io.Serializable{
    /**
     * 偏梯度
     * @param input 输入要训练的数据
     * @param targetVal 标签数据
     * @return
     */
    MultiDim prtGrad(MultiDim input, MultiDim targetVal);

    /**
     * 正向计算
     * @param input 输入要训练的数据
     * @param targetVal 标签数据
     * @param out 输出计算结果
     */
    void calc(MultiDim input,MultiDim targetVal,MultiDim out);
}
