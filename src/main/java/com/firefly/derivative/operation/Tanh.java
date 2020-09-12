package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationUnary;

/**
 * tanh
 */
public class Tanh extends OperationUnary {
    private double val=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Tanh(){

    }

    public Tanh(Function val){
        super(val);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getVal().isDx(dx)){
                double fv=calc();
                val=1-fv*fv;
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
        if(val!=v){
            val=v;
            double ex=Math.exp(v);
            double ex_=Math.exp(-v);

            calcVal=(ex-ex_)/(ex+ex_);
        }
        return calcVal;
    }
}