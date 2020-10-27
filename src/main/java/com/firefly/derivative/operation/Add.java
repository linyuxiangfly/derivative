package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationBinary;

/**
 * 加法
 */
public class Add extends OperationBinary {
    private static final long serialVersionUID = 1L;

    private double calcA=Double.MAX_VALUE,calcB=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Add(){

    }

    public Add(double a, double b) {
        super(a, b);
    }

    public Add(Function a, double b) {
        super(a, b);
    }

    public Add(double a, Function b) {
        super(a, b);
    }

    public Add(Function a, Function b){
        super(a,b);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;
        if(this==dx){
            val=1;
        }else{
            if(this.getA().isDx(dx)){
                val+=prtGradExA(dx,1.0);
            }
            if(this.getB().isDx(dx)){
                val+=prtGradExB(dx,1.0);
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
            calcVal=a+b;
        }
        return calcVal;
//        return this.getA().calc()+this.getB().calc();
    }
}
