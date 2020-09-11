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

    public AddMult(Function[] params){
        super(params);
    }

    @Override
    public double der(Function dx) {
        double val=0;
        if(this==dx){
            val=1;
        }else{
            if(this.getParams()!=null && this.getParams().length>0){
                for(Function param:this.getParams()){
                    if(param.isDx(dx)){
                        val+=this.derEx(param,dx,1.0);
                    }
                }
            }
        }

        return val;
    }
}
