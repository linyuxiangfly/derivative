package com.firefly.layers.data;

/**
 * 2维
 */
public class TwoDimShape extends Shape{
    public TwoDimShape(int oneDimDataNum, int twoDimDataNum) {
        int[] dimDataNum = new int[2];
        dimDataNum[0]=oneDimDataNum;
        dimDataNum[1]=twoDimDataNum;
        this.setDims(dimDataNum);
    }
}
