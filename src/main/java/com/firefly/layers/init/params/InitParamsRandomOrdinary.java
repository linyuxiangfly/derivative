package com.firefly.layers.init.params;

import com.firefly.layers.listeners.InitParamsListener;

import java.util.Random;

/**
 * 普通的随机数
 */
public class InitParamsRandomOrdinary implements InitParamsListener {
    @Override
    public void paramWSize(int row, int col) {
    }

    @Override
    public void paramBSize(int row) {
    }

    @Override
    public double initParamW(int i, int j) {
        return Math.random();
    }

    @Override
    public double initParamB(int i) {
        return Math.random();
    }
}
