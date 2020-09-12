package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationBinary;
import com.firefly.derivative.util.MathEx;

/**
 * 幂函数
 */
public class Power extends OperationBinary {
    private double calcA=Double.MAX_VALUE,calcB=Double.MAX_VALUE,calcVal=Double.MAX_VALUE;
//    private double derA_A,derA_B,derA_Val;
//    private Function derADx;
//
//    private double derB_A,derB_B,derB_Val;
//    private Function derBDx;

    public Power(){

    }

    public Power(Function a,Function b){
        super(a,b);
    }

    @Override
    public double prtGrad(Function dx) {
        double a=this.getA().calc();
        double b=this.getB().calc();

        double val=0;

        if(this==dx){
            val=1;
        }else{
            if(this.getA().isDx(dx)){
                val+=prtGradExA(dx,b*MathEx.pow(a,b-1));
            }
            if(this.getB().isDx(dx)){
                val+=prtGradExB(dx,calc()*Math.log(a));
            }
        }

        return val;
    }
//    public double prtGrad(Function dx) {
//        double a=this.getA().calc();
//        double b=this.getB().calc();
//
//        double val=0;
//        if(this.getA().isDx(dx)){
//            if(derA_A!=a || derA_B!=b || derADx!=dx){
//                derA_A=a;
//                derA_B=b;
//                derADx=dx;
//                derA_Val=b*Math.pow(a,b-1);
//            }else{
//                System.out.println();
//            }
////            val=b*Math.pow(a,b-1);
//            val=derA_Val;
//        }else if(this.getB().isDx(dx)){
//            if(derB_A!=a || derB_B!=b || derBDx!=dx){
//                derB_A=a;
//                derB_B=b;
//                derBDx=dx;
//                derB_Val=calc()*Math.log(a);
//            }else{
//                System.out.println();
//            }
////            val=calc()*Math.log(a);
//            val=derB_Val;
//        }
//
//        return prtGradEx(
//                dx,
//                val
//        );
//    }

    @Override
    public double calc() {
        double a=this.getA().calc();
        double b=this.getB().calc();
        if(calcA!=a || calcB!=b){
            calcA=a;
            calcB=b;
            calcVal=MathEx.pow(a,b);
        }
        return calcVal;
    }
}
