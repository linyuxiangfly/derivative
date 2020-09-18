package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;

/**
 * relu
 */
public class Relu extends OperationActivation {
    @Override
    public double prtGrad(Function dx) {
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getVal().isDx(dx)){
                double v=this.getVal().calc();
                val=v>0?1:0;
                val=prtGradEx(
                        dx,
                        val
                );
            }
        }

        return val;
    }

    @Override
    public double calc() {
        double v=this.getVal().calc();
        //f(v)=max(0,v)
        return v>0?v:0;
    }
}
