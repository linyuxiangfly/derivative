package com.firefly.layers.listeners;

/**
 * 拟合情况监控
 */
public interface FitControl extends java.io.Serializable{
    boolean onIsStop(int process,int epoch,double loss,long takeUpTime);
}
