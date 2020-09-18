package com.firefly.layers.listeners;

/**
 * 初始化参数事件
 */
public interface InitParamsListener {
    void paramWSize(int row,int col);
    void paramBSize(int row);

    //初始化参数W
    double initParamW(int i,int j);

    //初始化参数B
    double initParamB(int i);
}
