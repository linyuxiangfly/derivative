package com.firefly.operation.impl;

import com.firefly.operation.core.DoubleUnary;

public class OperationSqrt implements DoubleUnary {

    @Override
    public double calc(double a) {
        return Math.sqrt(a);
    }
}
