package com.firefly.operation.impl;

import com.firefly.operation.core.DoubleBinary;

public class OperationDiv implements DoubleBinary {

    @Override
    public double calc(double a, double b) {
        return a/b;
    }
}
