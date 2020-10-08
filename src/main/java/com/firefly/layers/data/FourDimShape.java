package com.firefly.layers.data;

/**
 * 4维
 */
public class FourDimShape extends Shape{
    public static final int W=0;//U轴
    public static final int X=1;//X轴
    public static final int Y=2;//Y轴
    public static final int Z=3;//Z轴

    public FourDimShape(int oneDimDataNum, int twoDimDataNum, int threeDimDataNum, int fourDimDataNum) {
        int[] dimDataNum = new int[4];
        dimDataNum[0]=oneDimDataNum;
        dimDataNum[1]=twoDimDataNum;
        dimDataNum[2]=threeDimDataNum;
        dimDataNum[3]=fourDimDataNum;
        this.setDims(dimDataNum);
    }

    public int getW(){
        return getDim(W);
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
