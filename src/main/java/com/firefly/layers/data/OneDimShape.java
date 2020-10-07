package com.firefly.layers.data;

/**
 * 1维
 */
public class OneDimShape extends Shape{
    public OneDimShape(int oneDimDataNum) {
        int[] dimDataNum = new int[1];
        dimDataNum[0]=oneDimDataNum;
        this.setDims(dimDataNum);
    }
}
