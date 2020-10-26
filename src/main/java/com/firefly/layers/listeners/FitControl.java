package com.firefly.layers.listeners;

/**
 * 拟合情况监控
 */
public interface FitControl extends java.io.Serializable{
    void onProcess(int process,int epoch,double currentProgress,double loss,long takeUpTime);
    boolean onIsStop(int process,int epoch,double loss,long takeUpTime);
}
