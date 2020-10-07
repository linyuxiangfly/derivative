package com.firefly.derivative.activation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.derivative.core.OperationUnary;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;

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

    public Softmax(double val, MultiDim relations){
        super(val,relations);
    }

    public Softmax(Function val,MultiDim relations){
        super(val,relations);
    }

    @Override
    public double prtGrad(Function dx) {
        return 0;
    }

    public double prtGrad(Function dx,MultiDim targetVal){
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

    private Function max(MultiDim vals){
        Function ret=null;
        ShapeIndex i=new ShapeIndex(vals.getShape());
        ShapeIndex index=new ShapeIndex(vals.getShape());

        double max=(double)vals.getVal(i);
        double temp=0;
        do{
            temp=(double)vals.getVal(i);
            if(temp>max){
                max=temp;
                index.copy(i);
            }
        }while (i.next());

        ret=(Function) this.getRelations().getVal(index);
        return ret;
    }

    private double sumRelations(){
        double ret=0;
        Function[] funcs=(Function[]) this.getRelations().getData();
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
