package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationBinary;

/**
 * 乘法
 */
public class Mcl extends OperationBinary {
    private double calcA=Double.MAX_VALUE,calcB=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Mcl(){

    }

    public Mcl(Function a, Function b){
        super(a,b);
    }

    @Override
    public double der(Function dx) {
        double val=0;
        if(this==dx){
            val=1;
        }else{
            if(this.getA().isDx(dx)){
                val+=derExA(dx,this.getB().calc());
            }
            if(this.getB().isDx(dx)){
                val+=derExB(dx,this.getA().calc());
            }
        }

        return val;
    }

    @Override
    public double calc() {
        double a=this.getA().calc();
        double b=this.getB().calc();
        if(calcA!=a || calcB!=b){
            calcA=a;
            calcB=b;
            calcVal=a*b;
        }
        return calcVal;
    }
}
