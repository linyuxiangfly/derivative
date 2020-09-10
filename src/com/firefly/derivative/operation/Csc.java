package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationUnary;
import com.firefly.derivative.util.MathEx;

/**
 * sec
 */
public class Csc extends OperationUnary {
    private double val=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Csc(){

    }

    public Csc(Function val){
        super(val);
    }

    @Override
    public double der(Function dx) {
        double v=this.getVal().calc();
        double val=0;
        if(this.getVal().isDx(dx)){
            val=-MathEx.csc(v)*MathEx.cot(v);
        }
        return derEx(
                dx,
                val
        );
    }

    @Override
    public double calc() {
        double v=this.getVal().calc();
        if(val!=v){
            val=v;
            calcVal=MathEx.csc(v);
        }
        return calcVal;
    }
}
