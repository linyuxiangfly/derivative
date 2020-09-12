package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationBinary;
import com.firefly.derivative.util.MathEx;

/**
 * log
 */
public class Log extends OperationBinary {
    private double calcA=Double.MAX_VALUE,calcB=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;

    public Log(){

    }

    public Log(double a, double b) {
        super(a, b);
    }

    public Log(Function a, double b) {
        super(a, b);
    }

    public Log(double a, Function b) {
        super(a, b);
    }

    public Log(Function a, Function b){
        super(a,b);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getA().isDx(dx)){
                val+=prtGradExA(dx,
                        -Math.log(this.getB().calc())
                                /
                                this.getA().calc()*MathEx.pow(Math.log(this.getA().calc()),2)
                );
            }
            if(this.getB().isDx(dx)){
                val+=prtGradExB(dx,
                        1.0
                                /
                                (this.getB().calc()*Math.log(this.getA().calc()))
                );
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
            calcVal=Math.log(a)/Math.log(b);
        }
        return calcVal;
    }
}
