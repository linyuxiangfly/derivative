package com.firefly.layers.loss;

import com.firefly.layers.core.Loss;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;
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
            double tv=(double)targetVal.getVal(i);
            if(tv!=0){
                ret-=tv*Math.log((double)input.getVal(i));
            }
        }while (i.next());

        ShapeIndex oi=new ShapeIndex(out.getShape());
        out.setVal(oi,ret);
    }

    @Override
    public MultiDim prtGrad(MultiDim input, MultiDim targetVal) {
        MultiDim ret=new MultiDim(input.getShape());
        ShapeIndex i=new ShapeIndex(input.getShape());
        do{
            double tv=(double)targetVal.getVal(i);
            if(tv!=0){
                double val=-tv/(double)input.getVal(i);
                ret.setVal(i,val);
            }
        }while (i.next());

        return ret;
    }
}
