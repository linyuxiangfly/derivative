package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationMultiple;

/**
 * 减法，多参数
 */
public class SubMult extends OperationMultiple {
    private static final long serialVersionUID = 1L;

    public SubMult(){

    }

    public SubMult(double[] params) {
        super(params);
    }

    public SubMult(Function[] params){
        super(params);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;
        if(this==dx){
            val=1;
        }else{
            if(this.getParams()!=null && this.getParams().length>0){
                int i=0;
                for(Function param:this.getParams()){
                    if(param.isDx(dx)){
                        val+=this.prtGradEx(param,dx,i==0?1.0:-1.0);
                    }
                    i++;
                }
            }
        }

        return val;
    }

    @Override
    public double calc() {
        double calcVal=0;
        for(int i=0;i<this.getParams().length;i++){
            if(i==0){
                calcVal=this.getParams()[i].calc();
            }else{
                calcVal-=this.getParams()[i].calc();
            }
        }
        return calcVal;
    }
}
