package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationUnary;
import com.firefly.derivative.util.MathEx;

/**
 * cot
 */
public class Cot extends OperationUnary {
    private double val=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Cot(){

    }

    public Cot(Function val){
        super(val);
    }

    @Override
    public double der(Function dx) {
        double val=0;
        if(this.getVal().isDx(dx)){
            val=-MathEx.pow(MathEx.csc(this.getVal().calc()),2);
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
            calcVal=MathEx.cot(v);
        }
        return calcVal;
    }
}
