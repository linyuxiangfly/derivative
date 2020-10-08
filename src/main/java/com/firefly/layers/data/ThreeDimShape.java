package com.firefly.layers.data;

/**
 * 3维
 */
public class ThreeDimShape extends Shape{
    public static final int X=0;//X轴
    public static final int Y=1;//Y轴
    public static final int Z=2;//Z轴

    public ThreeDimShape(int oneDimDataNum, int twoDimDataNum, int threeDimDataNum) {
        int[] dimDataNum = new int[3];
        dimDataNum[0]=oneDimDataNum;
        dimDataNum[1]=twoDimDataNum;
        dimDataNum[2]=threeDimDataNum;
        this.setDims(dimDataNum);
    }

    public int getX(){
        return getDim(X);
    }

    public int getY(){
        return getDim(Y);
    }

    public int getZ(){
        return getDim(Z);
    }
}
