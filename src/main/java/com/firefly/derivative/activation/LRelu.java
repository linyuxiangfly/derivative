package com.firefly.derivative.activation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationActivation;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;

/**
 * leaky relu
 */
public class LRelu extends OperationActivation {
    private Function minVal=null;

    private double getMinVal(){
        if(minVal==null && this.getSettings()!=null){
            ShapeIndex si=new ShapeIndex(this.getSettings().getShape());
            minVal=(Function)this.getSettings().getVal(si);
        }
        if(minVal!=null){
            return minVal.calc();
        }else{
            return 0;
        }
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getVal().isDx(dx)){
                double v=this.getVal().calc();
                if(v>=0){
                    val=1;
                }else{
                    val=getMinVal();//获取参数
                }
                val=prtGradEx(
                        dx,
                        val
                );
            }
        }

        return val;
    }

    public double prtGrad(Function dx, MultiDim targetVal){
        return prtGrad(dx);
    }

    @Override
    public double calc() {
        double v=this.getVal().calc();
        //f(v)=max(0,v)
        return v>=0?v:0;
    }
}
