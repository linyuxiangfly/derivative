package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationUnary;

/**
 * cos
 */
public class Cos extends OperationUnary {
    private static final long serialVersionUID = 1L;

    private double val=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Cos(){

    }

    public Cos(double val) {
        super(val);
    }

    public Cos(Function val){
        super(val);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getVal().isDx(dx)){
                val=-Math.sin(this.getVal().calc());
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
            calcVal=Math.cos(v);
        }
        return calcVal;
    }
}
