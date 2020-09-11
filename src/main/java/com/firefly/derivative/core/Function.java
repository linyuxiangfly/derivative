package com.firefly.derivative.core;

/**
 * 函数
 */
public interface Function{
    boolean isDx(Function dx);
    double der(Function dx);
    double calc();
}
