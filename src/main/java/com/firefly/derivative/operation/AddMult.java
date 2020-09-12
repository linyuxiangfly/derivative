package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationBinary;
import com.firefly.derivative.core.OperationMultiple;

/**
 * 加法，多参数
 */
public class AddMult extends OperationMultiple {
    public AddMult(){

    }

    public AddMult(double[] params) {
        super(params);
    }

    public AddMult(Function[] params){
        super(params);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;
        if(this==dx){
            val=1;
        }else{
            if(this.getParams()!=null && this.getParams().length>0){
                for(Function param:this.getParams()){
                    if(param.isDx(dx)){
                        val+=this.prtGradEx(param,dx,1.0);
                    }
                }
            }
        }

        return val;
    }

    @Override
    public double calc() {
        double calcVal=0;
        for(int i=0;i<this.getParams().length;i++){
            calcVal+=this.getParams()[i].calc();
        }
        return calcVal;
    }
}
