package com.firefly.operation.impl;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.operation.core.DoubleUnary;

public class OperationSqrt implements DoubleUnary {

    @Override
    public double calc(double a) {
        return Math.sqrt(a);
    }

    @Override
    public void calc(double[] a, double[] out) {
        for(int i=0;i<a.length;i++){
            out[i]=Math.sqrt(a[i]);
        }
    }

    @Override
    public void calc(double[][] a, double[][] out) {
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                out[i][j]=Math.sqrt(a[i][j]);
            }
        }
    }

    @Override
    public void calc(double[][][] a, double[][][] out) {
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    out[i][j][k]=Math.sqrt(a[i][j][k]);
                }
            }
        }
    }

    @Override
    public void calc(double[][][][] a, double[][][][] out) {
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    for(int l=0;l<a[i][j][k].length;l++){
                        out[i][j][k][l]=Math.sqrt(a[i][j][k][l]);
                    }
                }
            }
        }
    }

    @Override
    public void calc(MultiDim a, MultiDim out) {
        switch (a.getShape().howManyDim()) {
            case 1:
                calc((double[]) a.getData(), (double[]) out.getData());
                break;
            case 2:
                calc((double[][]) a.getData(), (double[][]) out.getData());
                break;
            case 3:
                calc((double[][][]) a.getData(), (double[][][]) out.getData());
                break;
            case 4:
                calc((double[][][][]) a.getData(), (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(a.getShape());
                do{
                    double aVal=(double)a.getVal(i);
                    out.setVal(i,Math.sqrt(aVal));
                }while (i.next());
                break;
        }
    }
}
