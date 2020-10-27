package com.firefly.operation.impl;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.operation.core.DoubleBinary;

public class OperationDiv implements DoubleBinary {
    private static final long serialVersionUID = 1L;

    @Override
    public double calc(double a, double b) {
        return a/b;
    }

    @Override
    public void calc(double[] a, double[] b, double[] out) {
        for(int i=0;i<a.length;i++){
            out[i]=a[i]/b[i];
        }
    }

    @Override
    public void calc(double[][] a, double[][] b, double[][] out) {
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                out[i][j]=a[i][j]/b[i][j];
            }
        }
    }

    @Override
    public void calc(double[][][] a, double[][][] b, double[][][] out) {
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    out[i][j][k]=a[i][j][k]/b[i][j][k];
                }
            }
        }
    }

    @Override
    public void calc(double[][][][] a, double[][][][] b, double[][][][] out) {
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    for(int l=0;l<a[i][j][k].length;l++){
                        out[i][j][k][l]=a[i][j][k][l]/b[i][j][k][l];
                    }
                }
            }
        }
    }

    @Override
    public void calc(MultiDim a, MultiDim b, MultiDim out) {
        switch (a.getShape().howManyDim()) {
            case 1:
                calc((double[]) a.getData(),(double[]) b.getData(), (double[]) out.getData());
                break;
            case 2:
                calc((double[][]) a.getData(),(double[][]) b.getData(), (double[][]) out.getData());
                break;
            case 3:
                calc((double[][][]) a.getData(),(double[][][]) b.getData(), (double[][][]) out.getData());
                break;
            case 4:
                calc((double[][][][]) a.getData(),(double[][][][]) b.getData(), (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(a.getShape());
                do{
                    double aVal=(double)a.getVal(i);
                    double bVal=(double)b.getVal(i);
                    out.setVal(i,aVal/bVal);
                }while (i.next());
                break;
        }
    }

    @Override
    public void calc(double[] a, double b, double[] out) {
        for(int i=0;i<a.length;i++){
            out[i]=a[i]/b;
        }
    }

    @Override
    public void calc(double[][] a, double b, double[][] out) {
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                out[i][j]=a[i][j]/b;
            }
        }
    }

    @Override
    public void calc(double[][][] a, double b, double[][][] out) {
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    out[i][j][k]=a[i][j][k]/b;
                }
            }
        }
    }

    @Override
    public void calc(double[][][][] a, double b, double[][][][] out) {
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    for(int l=0;l<a[i][j][k].length;l++){
                        out[i][j][k][l]=a[i][j][k][l]/b;
                    }
                }
            }
        }
    }

    @Override
    public void calc(MultiDim a, double b, MultiDim out) {
        switch (a.getShape().howManyDim()) {
            case 1:
                calc((double[]) a.getData(),b, (double[]) out.getData());
                break;
            case 2:
                calc((double[][]) a.getData(),b, (double[][]) out.getData());
                break;
            case 3:
                calc((double[][][]) a.getData(),b, (double[][][]) out.getData());
                break;
            case 4:
                calc((double[][][][]) a.getData(),b, (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(a.getShape());
                do{
                    double aVal=(double)a.getVal(i);
                    out.setVal(i,aVal/b);
                }while (i.next());
                break;
        }
    }

    @Override
    public void calc(double a, double[] b, double[] out) {
        for(int i=0;i<b.length;i++){
            out[i]=a/b[i];
        }
    }

    @Override
    public void calc(double a, double[][] b, double[][] out) {
        for(int i=0;i<b.length;i++){
            for(int j=0;j<b[i].length;j++){
                out[i][j]=a/b[i][j];
            }
        }
    }

    @Override
    public void calc(double a, double[][][] b, double[][][] out) {
        for(int i=0;i<b.length;i++){
            for(int j=0;j<b[i].length;j++){
                for(int k=0;k<b[i][j].length;k++){
                    out[i][j][k]=a/b[i][j][k];
                }
            }
        }
    }

    @Override
    public void calc(double a, double[][][][] b, double[][][][] out) {
        for(int i=0;i<b.length;i++){
            for(int j=0;j<b[i].length;j++){
                for(int k=0;k<b[i][j].length;k++){
                    for(int l=0;l<b[i][j][k].length;l++){
                        out[i][j][k][l]=a/b[i][j][k][l];
                    }
                }
            }
        }
    }

    @Override
    public void calc(double a, MultiDim b, MultiDim out) {
        switch (b.getShape().howManyDim()) {
            case 1:
                calc(a,(double[]) b.getData(), (double[]) out.getData());
                break;
            case 2:
                calc(a,(double[][]) b.getData(), (double[][]) out.getData());
                break;
            case 3:
                calc(a,(double[][][]) b.getData(), (double[][][]) out.getData());
                break;
            case 4:
                calc(a,(double[][][][]) b.getData(), (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(b.getShape());
                do{
                    double bVal=(double)b.getVal(i);
                    out.setVal(i,a/bVal);
                }while (i.next());
                break;
        }
    }
}
