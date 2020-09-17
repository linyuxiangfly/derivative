package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.derivative.core.OperationUnary;

/**
 * softmax
 */
public class Softmax extends OperationActivation {
    public Softmax(){

    }

    public Softmax(double val){
        super(val);
    }

    public Softmax(Function val){
        super(val);
    }

    public Softmax(double val,double[] relations){
        super(val,relations);
    }

    public Softmax(double val,Function[] relations){
        super(val,relations);
    }

    public Softmax(Function val,double[] relations){
        super(val,relations);
    }

    public Softmax(Function val,Function[] relations){
        super(val,relations);
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

    private double sumRelations(){
        double ret=0;
        Function[] funcs=this.getRelations();
        for(Function func:funcs){
            ret+=Math.exp(func.calc());
        }
        return ret;
    }

    @Override
    public double calc() {
        double v=Math.exp(this.getVal().calc());
        double sr=sumRelations();
        return v/sr;
    }
}
