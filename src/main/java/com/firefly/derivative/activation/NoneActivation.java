package com.firefly.derivative.activation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.layers.data.MultiDim;

/**
 * 不需要激活函数
 */
public class NoneActivation extends OperationActivation {
    public NoneActivation(){

    }

    public NoneActivation(double val) {
        super(val);
    }

    public NoneActivation(Function val){
        super(val);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getVal().isDx(dx)){
                val=prtGradEx(
                        dx,
                        1
                );
            }
        }

        return val;
    }

    public double prtGrad(Function dx, MultiDim targetVal){
        return prtGrad(dx);
    }

    @Override
    public double calc() {
        return this.getVal().calc();
    }
}
