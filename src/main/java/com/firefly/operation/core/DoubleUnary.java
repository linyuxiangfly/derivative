package com.firefly.operation.core;

import com.firefly.layers.data.MultiDim;

/**
 * double型的单目操作
 */
public interface DoubleUnary {
    double calc(double a);
    void calc(double[] a,double[] out);
    void calc(double[][] a,double[][] out);
    void calc(double[][][] a,double[][][] out);
    void calc(double[][][][] a,double[][][][] out);
    void calc(MultiDim a,MultiDim out);
}
