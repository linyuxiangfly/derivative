package com.firefly.layers.enums;

/**
 * 卷积填充类型
 */
public enum Padding {
    valid,//不填充
    same,//当滑动步长大于1时：填充数=K-I%S（K:卷积核边长，I：输入图像边长，S：滑动步长），滑动步长为1时，填充数是卷积核边长减1
}
