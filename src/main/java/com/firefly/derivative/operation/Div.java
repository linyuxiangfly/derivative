package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationBinary;
import com.firefly.derivative.util.MathEx;

/**
 * 除法
 */
public class Div extends OperationBinary {
    private static final long serialVersionUID = 1L;

    private double calcA=Double.MAX_VALUE,calcB=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Div(){

    }

    public Div(double a, double b) {
        super(a, b);
    }

    public Div(Function a, double b) {
        super(a, b);
    }

    public Div(double a, Function b) {
        super(a, b);
    }

    public Div(Function a, Function b){
        super(a,b);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getA().isDx(dx)){
                val+=prtGradExA(dx,1/this.getB().calc());
            }
            if(this.getB().isDx(dx)){
                val+=prtGradExB(dx,-this.getA().calc()/MathEx.pow(this.getB().calc(),2));
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
            calcVal=a/b;
        }
        return calcVal;
    }
}
