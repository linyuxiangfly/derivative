package com.firefly.derivative.activation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;

/**
 * leaky relu
 */
public class LRelu extends OperationActivation {
    @Override
    public double prtGrad(Function dx) {
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getVal().isDx(dx)){
                double v=this.getVal().calc();
                if(v>=0){
                    val=1;
                }else{
                    val=this.getSettings()[0].calc();//获取参数
                }
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
        return v>=0?v:0;
    }
}
