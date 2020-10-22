package com.firefly.utils;

import com.firefly.layers.data.MultiDim;
import com.firefly.operation.impl.*;

public class MultiDimUtil {
    private static final OperationAdd operationAdd=new OperationAdd();
    private static final OperationSub operationSub=new OperationSub();
    private static final OperationMcl operationMcl=new OperationMcl();
    private static final OperationDiv operationDiv=new OperationDiv();
    private static final OperationSqrt operationSqrt=new OperationSqrt();

    public static void sub(double[] a,double[] b,double[] out){
        operationSub.calc(a,b,out);
    }

    public static void sub(double[][] a,double[][] b,double[][] out){
        operationSub.calc(a,b,out);
    }

    public static void sub(double[][][] a,double[][][] b,double[][][] out){
        operationSub.calc(a,b,out);
    }

    public static void sub(double[][][][] a,double[][][][] b,double[][][][] out){
        operationSub.calc(a,b,out);
    }

    public static void sub(MultiDim a,MultiDim b,MultiDim out){
        operationSub.calc(a,b,out);
    }

    public static void add(double[] a,double[] b,double[] out){
        operationAdd.calc(a,b,out);
    }

    public static void add(double[][] a,double[][] b,double[][] out){
        operationAdd.calc(a,b,out);
    }

    public static void add(double[][][] a,double[][][] b,double[][][] out){
        operationAdd.calc(a,b,out);
    }

    public static void add(double[][][][] a,double[][][][] b,double[][][][] out){
        operationAdd.calc(a,b,out);
    }

    public static void add(MultiDim a,MultiDim b,MultiDim out){
        operationAdd.calc(a,b,out);
    }

    public static void add(double[] a,double b,double[] out){
        operationAdd.calc(a,b,out);
    }

    public static void add(double[][] a,double b,double[][] out){
        operationAdd.calc(a,b,out);
    }

    public static void add(double[][][] a,double b,double[][][] out){
        operationAdd.calc(a,b,out);
    }

    public static void add(double[][][][] a,double b,double[][][][] out){
        operationAdd.calc(a,b,out);
    }

    public static void add(MultiDim a,double b,MultiDim out){
        operationAdd.calc(a,b,out);
    }

    public static void mcl(double[] a,double b,double[] out){
        operationMcl.calc(a,b,out);
    }

    public static void mcl(double[][] a,double b,double[][] out){
        operationMcl.calc(a,b,out);
    }

    public static void mcl(double[][][] a,double b,double[][][] out){
        operationMcl.calc(a,b,out);
    }

    public static void mcl(double[][][][] a,double b,double[][][][] out){
        operationMcl.calc(a,b,out);
    }

    public static void mcl(MultiDim a,double b,MultiDim out){
        operationMcl.calc(a,b,out);
    }

    public static void mcl(double[] a,double[] b,double[] out){
        operationMcl.calc(a,b,out);
    }

    public static void mcl(double[][] a,double[][] b,double[][] out){
        operationMcl.calc(a,b,out);
    }

    public static void mcl(double[][][] a,double[][][] b,double[][][] out){
        operationMcl.calc(a,b,out);
    }

    public static void mcl(double[][][][] a,double[][][][] b,double[][][][] out){
        operationMcl.calc(a,b,out);
    }

    public static void mcl(MultiDim a,MultiDim b,MultiDim out){
        operationMcl.calc(a,b,out);
    }

    public static void div(double[] a,double[] b,double[] out){
        operationDiv.calc(a,b,out);
    }

    public static void div(double[][] a,double[][] b,double[][] out){
        operationDiv.calc(a,b,out);
    }

    public static void div(double[][][] a,double[][][] b,double[][][] out){
        operationDiv.calc(a,b,out);
    }

    public static void div(double[][][][] a,double[][][][] b,double[][][][] out){
        operationDiv.calc(a,b,out);
    }

    public static void div(MultiDim a,MultiDim b,MultiDim out){
        operationDiv.calc(a,b,out);
    }

    public static void sqrt(double[] a,double[] out){
        operationSqrt.calc(a,out);
    }

    public static void sqrt(double[][] a,double[][] out){
        operationSqrt.calc(a,out);
    }

    public static void sqrt(double[][][] a,double[][][] out){
        operationSqrt.calc(a,out);
    }

    public static void sqrt(double[][][][] a,double[][][][] out){
        operationSqrt.calc(a,out);
    }

    public static void sqrt(MultiDim a,MultiDim out){
        operationSqrt.calc(a,out);
    }
}
