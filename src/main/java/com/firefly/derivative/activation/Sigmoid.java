package com.firefly.derivative.activation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.derivative.core.OperationUnary;

/**
 * sigmoid
 */
public class Sigmoid extends OperationActivation {
    private double val=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Sigmoid(){

    }

    public Sigmoid(double val) {
        super(val);
    }

    public Sigmoid(Function val){
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
                val=fv*(1-fv);
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
            calcVal=1/(1+Math.exp(-v));
        }
        return calcVal;
    }
}
