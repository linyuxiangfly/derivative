package com.firefly.layers.init.params;

import com.firefly.layers.listeners.InitParamsListener;

import java.util.Random;

/**
 * 标准正态随机分布
 */
public class InitParamsRandomGaussian implements InitParamsListener {
    private Random[] randomW;
    private Random randomB;

    @Override
    public void paramWSize(int row, int col) {
        randomW=new Random[row];
        for(int i=0;i<row;i++){
            randomW[i]=new Random();
        }
    }

    @Override
    public void paramBSize(int row) {
        randomB=new Random();
    }

    @Override
    public double initParamW(int i, int j) {
        return randomW[i].nextGaussian();
    }

    @Override
    public double initParamB(int i) {
        return randomB.nextGaussian();
    }
}
