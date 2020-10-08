package com.firefly.layers.data;

/**
 * 1维
 */
public class OneDimShape extends Shape{
    public static final int X=0;//X轴

    public OneDimShape(int oneDimDataNum) {
        int[] dimDataNum = new int[1];
        dimDataNum[0]=oneDimDataNum;
        this.setDims(dimDataNum);
    }

    public int getX(){
        return getDim(X);
    }
}
