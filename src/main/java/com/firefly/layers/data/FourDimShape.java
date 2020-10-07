package com.firefly.layers.data;

/**
 * 4维
 */
public class FourDimShape extends Shape{
    public FourDimShape(int oneDimDataNum, int twoDimDataNum, int threeDimDataNum, int fourDimDataNum) {
        int[] dimDataNum = new int[4];
        dimDataNum[0]=oneDimDataNum;
        dimDataNum[1]=twoDimDataNum;
        dimDataNum[2]=threeDimDataNum;
        dimDataNum[3]=fourDimDataNum;
        this.setDims(dimDataNum);
    }
}
