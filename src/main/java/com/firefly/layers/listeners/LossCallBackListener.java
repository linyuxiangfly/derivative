package com.firefly.layers.listeners;

/**
 * 识差回调
 */
public interface LossCallBackListener extends java.io.Serializable{
    /**
     * 识差
     * @param val
     */
    void onLoss(double val);
}
