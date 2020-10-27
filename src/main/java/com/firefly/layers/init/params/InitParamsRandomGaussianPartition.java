package com.firefly.layers.init.params;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.listeners.InitParamsListener;

import java.util.Random;

/**
 * 标准正态随机分布，每个参数都是隔离的
 */
public class InitParamsRandomGaussianPartition implements InitParamsListener {
    private static final long serialVersionUID = 1L;

    private MultiDim randomW;
    private MultiDim randomB;

    @Override
    public void paramWSize(Shape shape) {
        randomW=new MultiDim(Random.class,shape);
        ShapeIndex i=new ShapeIndex(shape);
        do{
            randomW.setVal(i,new Random());
        }while(i.next());
    }

    @Override
    public void paramBSize(Shape shape) {
        randomB=new MultiDim(Random.class,shape);
        ShapeIndex i=new ShapeIndex(shape);
        do{
            randomB.setVal(i,new Random());
        }while(i.next());
    }

    @Override
    public double initParamW(ShapeIndex shapeIndex) {
        return ((Random)randomW.getVal(shapeIndex)).nextGaussian();
    }

    @Override
    public double initParamB(ShapeIndex shapeIndex) {
        return ((Random)randomB.getVal(shapeIndex)).nextGaussian();
    }
}
