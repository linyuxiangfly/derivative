package com.firefly.layers.listeners;

import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;

/**
 * 初始化参数事件
 */
public interface InitParamsListener extends java.io.Serializable{
    void paramWSize(Shape shape);
    void paramBSize(Shape shape);

    //初始化参数W
    double initParamW(ShapeIndex shapeIndex);

    //初始化参数B
    double initParamB(ShapeIndex shapeIndex);
}
