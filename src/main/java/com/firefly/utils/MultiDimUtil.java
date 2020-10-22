package com.firefly.utils;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.operation.core.DoubleBinary;
import com.firefly.operation.core.DoubleUnary;
import com.firefly.operation.impl.*;

public class MultiDimUtil {
    private static final OperationAdd operationAdd=new OperationAdd();
    private static final OperationSub operationSub=new OperationSub();
    private static final OperationMcl operationMcl=new OperationMcl();
    private static final OperationDiv operationDiv=new OperationDiv();
    private static final OperationSqrt operationSqrt=new OperationSqrt();

    public static void sub(double[] a,double[] b,double[] out){
        operation(operationSub,a,b,out);
    }

    public static void sub(double[][] a,double[][] b,double[][] out){
        operation(operationSub,a,b,out);
    }

    public static void sub(double[][][] a,double[][][] b,double[][][] out){
        operation(operationSub,a,b,out);
    }

    public static void sub(double[][][][] a,double[][][][] b,double[][][][] out){
        operation(operationSub,a,b,out);
    }

    public static void sub(MultiDim a,MultiDim b,MultiDim out){
        operation(operationSub,a,b,out);
    }

    public static void add(double[] a,double[] b,double[] out){
        operation(operationAdd,a,b,out);
    }

    public static void add(double[][] a,double[][] b,double[][] out){
        operation(operationAdd,a,b,out);
    }

    public static void add(double[][][] a,double[][][] b,double[][][] out){
        operation(operationAdd,a,b,out);
    }

    public static void add(double[][][][] a,double[][][][] b,double[][][][] out){
        operation(operationAdd,a,b,out);
    }

    public static void add(MultiDim a,MultiDim b,MultiDim out){
        operation(operationAdd,a,b,out);
    }

    public static void add(double[] a,double b,double[] out){
        operation(operationAdd,a,b,out);
    }

    public static void add(double[][] a,double b,double[][] out){
        operation(operationAdd,a,b,out);
    }

    public static void add(double[][][] a,double b,double[][][] out){
        operation(operationAdd,a,b,out);
    }

    public static void add(double[][][][] a,double b,double[][][][] out){
        operation(operationAdd,a,b,out);
    }

    public static void add(MultiDim a,double b,MultiDim out){
        operation(operationAdd,a,b,out);
    }

    public static void mcl(double[] a,double b,double[] out){
        operation(operationMcl,a,b,out);
    }

    public static void mcl(double[][] a,double b,double[][] out){
        operation(operationMcl,a,b,out);
    }

    public static void mcl(double[][][] a,double b,double[][][] out){
        operation(operationMcl,a,b,out);
    }

    public static void mcl(double[][][][] a,double b,double[][][][] out){
        operation(operationMcl,a,b,out);
    }

    public static void mcl(MultiDim a,double b,MultiDim out){
        operation(operationMcl,a,b,out);
    }

    public static void mcl(double[] a,double[] b,double[] out){
        operation(operationMcl,a,b,out);
    }

    public static void mcl(double[][] a,double[][] b,double[][] out){
        operation(operationMcl,a,b,out);
    }

    public static void mcl(double[][][] a,double[][][] b,double[][][] out){
        operation(operationMcl,a,b,out);
    }

    public static void mcl(double[][][][] a,double[][][][] b,double[][][][] out){
        operation(operationMcl,a,b,out);
    }

    public static void mcl(MultiDim a,MultiDim b,MultiDim out){
        operation(operationMcl,a,b,out);
    }

    public static void div(double[] a,double[] b,double[] out){
        operation(operationDiv,a,b,out);
    }

    public static void div(double[][] a,double[][] b,double[][] out){
        operation(operationDiv,a,b,out);
    }

    public static void div(double[][][] a,double[][][] b,double[][][] out){
        operation(operationDiv,a,b,out);
    }

    public static void div(double[][][][] a,double[][][][] b,double[][][][] out){
        operation(operationDiv,a,b,out);
    }

    public static void div(MultiDim a,MultiDim b,MultiDim out){
        operation(operationDiv,a,b,out);
    }

    public static void sqrt(double[] a,double[] out){
        operation(operationSqrt,a,out);
    }

    public static void sqrt(double[][] a,double[][] out){
        operation(operationSqrt,a,out);
    }

    public static void sqrt(double[][][] a,double[][][] out){
        operation(operationSqrt,a,out);
    }

    public static void sqrt(double[][][][] a,double[][][][] out){
        operation(operationSqrt,a,out);
    }

    public static void sqrt(MultiDim a,MultiDim out){
        operation(operationSqrt,a,out);
    }

    public static void operation(DoubleBinary func,double[] a,double[] b,double[] out){
        for(int i=0;i<a.length;i++){
            out[i]=func.calc(a[i],b[i]);
        }
    }

    public static void operation(DoubleBinary func,double[][] a,double[][] b,double[][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                out[i][j]=func.calc(a[i][j],b[i][j]);
            }
        }
    }

    public static void operation(DoubleBinary func,double[][][] a,double[][][] b,double[][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    out[i][j][k]=func.calc(a[i][j][k],b[i][j][k]);
                }
            }
        }
    }

    public static void operation(DoubleBinary func,double[][][][] a,double[][][][] b,double[][][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    for(int l=0;l<a[i][j][k].length;l++){
                        out[i][j][k][l]=func.calc(a[i][j][k][l],b[i][j][k][l]);
                    }
                }
            }
        }
    }

    public static void operation(DoubleBinary func, MultiDim a,MultiDim b, MultiDim out){
        switch (a.getShape().howManyDim()) {
            case 1:
                operation(func,(double[]) a.getData(),(double[]) b.getData(), (double[]) out.getData());
                break;
            case 2:
                operation(func,(double[][]) a.getData(),(double[][]) b.getData(), (double[][]) out.getData());
                break;
            case 3:
                operation(func,(double[][][]) a.getData(),(double[][][]) b.getData(), (double[][][]) out.getData());
                break;
            case 4:
                operation(func,(double[][][][]) a.getData(),(double[][][][]) b.getData(), (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(a.getShape());
                do{
                    double aVal=(double)a.getVal(i);
                    double bVal=(double)b.getVal(i);
                    out.setVal(i,func.calc(aVal,bVal));
                }while (i.next());
                break;
        }
    }

    public static void operation(DoubleBinary func,double[] a,double b,double[] out){
        for(int i=0;i<a.length;i++){
            out[i]=func.calc(a[i],b);
        }
    }

    public static void operation(DoubleBinary func,double[][] a,double b,double[][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                out[i][j]=func.calc(a[i][j],b);
            }
        }
    }

    public static void operation(DoubleBinary func,double[][][] a,double b,double[][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    out[i][j][k]=func.calc(a[i][j][k],b);
                }
            }
        }
    }

    public static void operation(DoubleBinary func,double[][][][] a,double b,double[][][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    for(int l=0;l<a[i][j][k].length;l++){
                        out[i][j][k][l]=func.calc(a[i][j][k][l],b);
                    }
                }
            }
        }
    }

    public static void operation(DoubleBinary func, MultiDim a,double b, MultiDim out){
        switch (a.getShape().howManyDim()) {
            case 1:
                operation(func,(double[]) a.getData(),b, (double[]) out.getData());
                break;
            case 2:
                operation(func,(double[][]) a.getData(),b, (double[][]) out.getData());
                break;
            case 3:
                operation(func,(double[][][]) a.getData(),b, (double[][][]) out.getData());
                break;
            case 4:
                operation(func,(double[][][][]) a.getData(),b, (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(a.getShape());
                do{
                    double aVal=(double)a.getVal(i);
                    out.setVal(i,func.calc(aVal,b));
                }while (i.next());
                break;
        }
    }

    public static void operation(DoubleBinary func,double a,double b[],double[] out){
        for(int i=0;i<b.length;i++){
            out[i]=func.calc(a,b[i]);
        }
    }

    public static void operation(DoubleBinary func,double a,double b[][],double[][] out){
        for(int i=0;i<b.length;i++){
            for(int j=0;j<b[i].length;j++){
                out[i][j]=func.calc(a,b[i][j]);
            }
        }
    }

    public static void operation(DoubleBinary func,double a,double b[][][],double[][][] out){
        for(int i=0;i<b.length;i++){
            for(int j=0;j<b[i].length;j++){
                for(int k=0;k<b[i][j].length;k++){
                    out[i][j][k]=func.calc(a,b[i][j][k]);
                }
            }
        }
    }

    public static void operation(DoubleBinary func,double a,double b[][][][],double[][][][] out){
        for(int i=0;i<b.length;i++){
            for(int j=0;j<b[i].length;j++){
                for(int k=0;k<b[i][j].length;k++){
                    for(int l=0;l<b[i][j][k].length;l++){
                        out[i][j][k][l]=func.calc(a,b[i][j][k][l]);
                    }
                }
            }
        }
    }

    public static void operation(DoubleBinary func, double a,MultiDim b, MultiDim out){
        switch (b.getShape().howManyDim()) {
            case 1:
                operation(func,a,(double[]) b.getData(), (double[]) out.getData());
                break;
            case 2:
                operation(func,a,(double[][]) b.getData(), (double[][]) out.getData());
                break;
            case 3:
                operation(func,a,(double[][][]) b.getData(), (double[][][]) out.getData());
                break;
            case 4:
                operation(func,a,(double[][][][]) b.getData(), (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(b.getShape());
                do{
                    double bVal=(double)b.getVal(i);
                    out.setVal(i,func.calc(a,bVal));
                }while (i.next());
                break;
        }
    }

    public static void operation(DoubleUnary func,double[] a,double[] out){
        for(int i=0;i<a.length;i++){
            out[i]=func.calc(a[i]);
        }
    }

    public static void operation(DoubleUnary func,double[][] a,double[][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                out[i][j]=func.calc(a[i][j]);
            }
        }
    }

    public static void operation(DoubleUnary func,double[][][] a,double[][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    out[i][j][k]=func.calc(a[i][j][k]);
                }
            }
        }
    }

    public static void operation(DoubleUnary func,double[][][][] a,double[][][][] out){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                for(int k=0;k<a[i][j].length;k++){
                    for(int l=0;l<a[i][j][k].length;l++){
                        out[i][j][k][l]=func.calc(a[i][j][k][l]);
                    }
                }
            }
        }
    }

    public static void operation(DoubleUnary func, MultiDim a, MultiDim out){
        switch (a.getShape().howManyDim()) {
            case 1:
                operation(func,(double[]) a.getData(), (double[]) out.getData());
                break;
            case 2:
                operation(func,(double[][]) a.getData(), (double[][]) out.getData());
                break;
            case 3:
                operation(func,(double[][][]) a.getData(), (double[][][]) out.getData());
                break;
            case 4:
                operation(func,(double[][][][]) a.getData(), (double[][][][]) out.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(a.getShape());
                do{
                    double aVal=(double)a.getVal(i);
                    out.setVal(i,func.calc(aVal));
                }while (i.next());
                break;
        }
    }
}
