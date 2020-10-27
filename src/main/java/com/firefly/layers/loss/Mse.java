package com.firefly.layers.loss;

import com.firefly.derivative.core.Function;
import com.firefly.layers.core.Loss;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.math.Linalg;

/**
 * 均方误差（MSE，mean squared error）
 */
public class Mse implements Loss {
    private static final long serialVersionUID = 1L;

    @Override
    public void calc(MultiDim input, MultiDim targetVal, MultiDim out) {
        MultiDim error=new MultiDim(input.getShape());
        ShapeIndex i=new ShapeIndex(input.getShape());

        do{
            error.setVal(i,(double)targetVal.getVal(i)-(double)input.getVal(i));
        }while(i.next());

        ShapeIndex oi=new ShapeIndex(out.getShape());
        out.setVal(oi,Linalg.inner(error, error)*0.5);
    }

    @Override
    public MultiDim prtGrad(MultiDim input,MultiDim targetVal) {
        return Linalg.sub(input,targetVal);
    }
}
