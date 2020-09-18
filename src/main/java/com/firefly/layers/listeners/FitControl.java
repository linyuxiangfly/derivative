package com.firefly.layers.listeners;

/**
 * 拟合情况监控
 */
public interface FitControl {
    boolean onIsStop(int epoch,double loss);
}
