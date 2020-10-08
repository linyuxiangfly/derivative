package com.firefly.layers.data;

/**
 * 2维
 */
public class TwoDimShape extends Shape{
    public static final int X=0;//X轴
    public static final int Y=1;//Y轴

    public TwoDimShape(int oneDimDataNum, int twoDimDataNum) {
        int[] dimDataNum = new int[2];
        dimDataNum[0]=oneDimDataNum;
        dimDataNum[1]=twoDimDataNum;
        this.setDims(dimDataNum);
    }

    public int getX(){
        return getDim(X);
    }

    public int getY(){
        return getDim(Y);
    }
}
