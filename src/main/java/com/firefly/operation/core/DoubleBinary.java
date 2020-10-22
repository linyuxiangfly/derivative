package com.firefly.operation.core;

import com.firefly.layers.data.MultiDim;

/**
 * double型的双目操作
 */
public interface DoubleBinary {
    double calc(double a,double b);

    void calc(double[] a,double[] b,double[] out);
    void calc(double[][] a,double[][] b,double[][] out);
    void calc(double[][][] a,double[][][] b,double[][][] out);
    void calc(double[][][][] a,double[][][][] b,double[][][][] out);
    void calc(MultiDim a, MultiDim b, MultiDim out);

    void calc(double[] a,double b,double[] out);
    void calc(double[][] a,double b,double[][] out);
    void calc(double[][][] a,double b,double[][][] out);
    void calc(double[][][][] a,double b,double[][][][] out);
    void calc(MultiDim a, double b, MultiDim out);

    void calc(double a,double[] b,double[] out);
    void calc(double a,double[][] b,double[][] out);
    void calc(double a,double[][][] b,double[][][] out);
    void calc(double a,double[][][][] b,double[][][][] out);
    void calc(double a, MultiDim b, MultiDim out);
}
