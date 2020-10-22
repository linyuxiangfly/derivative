package com.firefly.operation.impl;

import com.firefly.operation.core.DoubleBinary;

public class OperationSub implements DoubleBinary {

    @Override
    public double calc(double a, double b) {
        return a-b;
    }
}
