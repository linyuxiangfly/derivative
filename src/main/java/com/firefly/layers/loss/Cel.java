package com.firefly.layers.loss;

import com.firefly.derivative.util.MathEx;
import com.firefly.layers.core.Loss;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.math.Linalg;
import com.firefly.math.Statistics;

/**
 * 交叉熵损失函数
 */
public class Cel implements Loss {
    @Override
    public void calc(MultiDim input,MultiDim targetVal,MultiDim out) {
        double ret=0;
//        double si=sumInput(input);//累计所有输入的和
        ShapeIndex i=new ShapeIndex(input.getShape());
        do{
            ret+=(double)targetVal.getVal(i)*Math.log((double)input.getVal(i));
        }while (i.next());

        ShapeIndex oi=new ShapeIndex(out.getShape());
        out.setVal(oi,-ret);
    }

    /**
     * 累计所有输入的值
     * @param input
     * @return
     */
    private double sumInput(double[] input){
        return Statistics.sum(input);
    }

    @Override
    public MultiDim prtGrad(MultiDim input, MultiDim targetVal) {
        MultiDim ret=new MultiDim(input.getShape());
        ShapeIndex i=new ShapeIndex(input.getShape());
        do{
            ret.setVal(i,-(double)targetVal.getVal(i)/(double)input.getVal(i));
        }while (i.next());

        return ret;
    }
}
