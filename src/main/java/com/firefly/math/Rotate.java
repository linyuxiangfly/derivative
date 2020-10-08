package com.firefly.math;

public class Rotate {
    public static void rtate90(double[][][] data){

    }

    public static void rtate180(double[][][] data){
        int rowNum=data.length;
        int colNum=data[0].length;

        //求出中间行
        int remnantRow=rowNum%2;
        int middleRow=(rowNum-remnantRow)/2;
        //求出中间列
        int remnantCol=colNum%2;
        int middleCol=(colNum-remnantCol)/2+remnantCol;

        for(int x=0;x<middleRow;x++){
            for(int y=0;y<colNum;y++){
                swap(data,x,y,rowNum-x-1,colNum-y-1);
            }
        }
        //中间行的交换
        for(int x=middleRow;x<middleRow+remnantRow;x++){
            for(int y=0;y<middleCol;y++){
                swap(data,x,y,rowNum-x-1,colNum-y-1);
            }
        }
    }

    /**
     * 将两个坐标的值调换
     * @param data
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     */
    private static void swap(double[][][] data,int row1,int col1,int row2,int col2){
        //相同坐标的值不用管
        if(row1==row2 && col1==col2){
            return;
        }
        double[] d1=data[row1][col1];
        data[row1][col1]=data[row2][col2];
        data[row2][col2]=d1;
    }

    public static void rtate270(double[][][] data){

    }
}
