package com.firefly.utils;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;

public class MultiDimUtil {
    public static void sub(double[] a,double[] b,double[] out){
        for(int i=0;i<a.length;i++){
            out[i]=a[i]-b[i];
        }
    }

    public static void sub(double[][] a,double[][] b,double[][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                out[i][j]=a[i][j]-b[i][j];
            }
        }
    }

    public static void sub(double[][][] a,double[][][] b,double[][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    out[i][j][k]=a[i][j][k]-b[i][j][k];
                }
            }
        }
    }

    public static void sub(double[][][][] a,double[][][][] b,double[][][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    for(int l=0;l<a[i][j][k].length;l++){
                        out[i][j][k][l]=a[i][j][k][l]-b[i][j][k][l];
                    }
                }
            }
        }
    }

    public static void sub(MultiDim a,MultiDim b,MultiDim out){
        switch (a.getShape().howManyDim()){
            case 1:
                sub((double[])a.getData(),(double[])b.getData(),(double[])out.getData());
                break;
            case 2:
                sub((double[][])a.getData(),(double[][])b.getData(),(double[][])out.getData());
                break;
            case 3:
                sub((double[][][])a.getData(),(double[][][])b.getData(),(double[][][])out.getData());
                break;
            case 4:
                sub((double[][][][])a.getData(),(double[][][][])b.getData(),(double[][][][])out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(a.getShape());
                do{
                    double aVal=(double)a.getVal(i);
                    double bVal=(double)b.getVal(i);
                    out.setVal(i,aVal-bVal);
                }while (i.next());
                break;
        }
    }

    public static void add(double[] a,double[] b,double[] out){
        for(int i=0;i<a.length;i++){
            out[i]=a[i]+b[i];
        }
    }

    public static void add(double[][] a,double[][] b,double[][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                out[i][j]=a[i][j]+b[i][j];
            }
        }
    }

    public static void add(double[][][] a,double[][][] b,double[][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    out[i][j][k]=a[i][j][k]+b[i][j][k];
                }
            }
        }
    }

    public static void add(double[][][][] a,double[][][][] b,double[][][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    for(int l=0;l<a[i][j][k].length;l++){
                        out[i][j][k][l]=a[i][j][k][l]+b[i][j][k][l];
                    }
                }
            }
        }
    }

    public static void add(MultiDim a,MultiDim b,MultiDim out){
        switch (a.getShape().howManyDim()) {
            case 1:
                add((double[]) a.getData(), (double[]) b.getData(), (double[]) out.getData());
                break;
            case 2:
                add((double[][]) a.getData(), (double[][]) b.getData(), (double[][]) out.getData());
                break;
            case 3:
                add((double[][][]) a.getData(), (double[][][]) b.getData(), (double[][][]) out.getData());
                break;
            case 4:
                add((double[][][][]) a.getData(), (double[][][][]) b.getData(), (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(a.getShape());
                do{
                    double aVal=(double)a.getVal(i);
                    double bVal=(double)b.getVal(i);
                    out.setVal(i,aVal+bVal);
                }while (i.next());
                break;
        }
    }

    public static void mcl(double[] a,double b,double[] out){
        for(int i=0;i<a.length;i++){
            out[i]=a[i]*b;
        }
    }

    public static void mcl(double[][] a,double b,double[][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                out[i][j]=a[i][j]*b;
            }
        }
    }

    public static void mcl(double[][][] a,double b,double[][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    out[i][j][k]=a[i][j][k]*b;
                }
            }
        }
    }

    public static void mcl(double[][][][] a,double b,double[][][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    for(int l=0;l<a[i][j][k].length;l++){
                        out[i][j][k][l]=a[i][j][k][l]*b;
                    }
                }
            }
        }
    }

    public static void mcl(MultiDim a,double b,MultiDim out){
        switch (a.getShape().howManyDim()) {
            case 1:
                mcl((double[]) a.getData(), b, (double[]) out.getData());
                break;
            case 2:
                mcl((double[][]) a.getData(), b, (double[][]) out.getData());
                break;
            case 3:
                mcl((double[][][]) a.getData(), b, (double[][][]) out.getData());
                break;
            case 4:
                mcl((double[][][][]) a.getData(), b, (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(a.getShape());
                do{
                    double aVal=(double)a.getVal(i);
                    out.setVal(i,aVal*b);
                }while (i.next());
                break;
        }
    }
}
