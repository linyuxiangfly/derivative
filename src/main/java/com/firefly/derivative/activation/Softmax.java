package com.firefly.derivative.activation;

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
        return 0;
    }

    public double prtGrad(Function dx,double[] targetVal){
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getVal().isDx(dx)){
                Function ajFunc=max(targetVal);
                double ai=calc();
                if(this.getVal()==ajFunc){
                    val=ai*(1-ai);
                }else{
                    val=-ajFunc.calc()*ai;
                }

                val=prtGradEx(
                        dx,
                        val
                );
            }
        }

        return val;
    }

    private Function max(double[] vals){
        Function ret=null;
        double max=vals[0];
        int index=0;
        Function[] funcs=this.getRelations();
        for(int i=0;i<vals.length;i++){
            if(vals[i]>max){
                max=vals[i];
                index=i;
            }
        }
        ret=funcs[index];
        return ret;
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
