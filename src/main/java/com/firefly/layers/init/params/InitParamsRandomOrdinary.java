package com.firefly.layers.init.params;

import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.listeners.InitParamsListener;

import java.util.Random;

/**
 * 普通的随机数
 */
public class InitParamsRandomOrdinary implements InitParamsListener {
    @Override
    public void paramWSize(Shape shape) {

    }

    @Override
    public void paramBSize(Shape shape) {

    }

    @Override
    public double initParamW(ShapeIndex shapeIndex) {
        return Math.random();
    }

    @Override
    public double initParamB(ShapeIndex shapeIndex) {
        return Math.random();
    }
}
