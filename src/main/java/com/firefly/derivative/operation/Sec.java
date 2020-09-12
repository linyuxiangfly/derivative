package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationUnary;
import com.firefly.derivative.util.MathEx;

/**
 * sec
 */
public class Sec extends OperationUnary {
    private double val=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Sec(){

    }

    public Sec(Function val){
        super(val);
    }

    @Override
    public double prtGrad(Function dx) {
        double v=this.getVal().calc();
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getVal().isDx(dx)){
                val=MathEx.sec(v)*Math.tan(v);
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
            calcVal=MathEx.sec(v);
        }
        return calcVal;
    }
}
