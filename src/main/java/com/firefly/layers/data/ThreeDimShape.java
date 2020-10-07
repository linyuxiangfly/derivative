package com.firefly.layers.data;

/**
 * 3维
 */
public class ThreeDimShape extends Shape{
    public ThreeDimShape(int oneDimDataNum, int twoDimDataNum, int threeDimDataNum) {
        int[] dimDataNum = new int[3];
        dimDataNum[0]=oneDimDataNum;
        dimDataNum[1]=twoDimDataNum;
        dimDataNum[2]=threeDimDataNum;
        this.setDims(dimDataNum);
    }
}
