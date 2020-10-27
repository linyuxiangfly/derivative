package com.firefly.layers.init.params;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.listeners.InitParamsListener;

import java.util.Random;

/**
 * 标准正态随机分布
 */
public class InitParamsRandomGaussian implements InitParamsListener {
    private static final long serialVersionUID = 1L;

    private Random random;

    public InitParamsRandomGaussian(){
        random=new Random();
    }

    @Override
    public void paramWSize(Shape shape) {

    }

    @Override
    public void paramBSize(Shape shape) {

    }

    @Override
    public double initParamW(ShapeIndex shapeIndex) {
        return random.nextGaussian();
    }

    @Override
    public double initParamB(ShapeIndex shapeIndex) {
        return random.nextGaussian();
    }
}
