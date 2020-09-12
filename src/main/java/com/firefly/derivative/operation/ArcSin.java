package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationUnary;
import com.firefly.derivative.util.MathEx;

/**
 * arcsin
 */
public class ArcSin extends OperationUnary {
    private double val=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;
    public ArcSin(){

    }

    public ArcSin(double val) {
        super(val);
    }

    public ArcSin(Function val){
        super(val);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getVal().isDx(dx)){
                val=1.0/MathEx.pow(1.0-MathEx.pow(this.getVal().calc(),2),0.5);
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
            calcVal=Math.asin(v);
        }
        return calcVal;
    }
}
