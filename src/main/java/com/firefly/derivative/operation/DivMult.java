package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationMultiple;

/**
 * 除法，多参数
 */
public class DivMult extends OperationMultiple {
    private static final long serialVersionUID = 1L;

    public DivMult(){

    }

    public DivMult(double[] params) {
        super(params);
    }

    public DivMult(Function[] params){
        super(params);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;
        if(this==dx){
            val=1;
        }else{
            Function[] params=this.getParams();
            if(params!=null && params.length>0){
                double[] pcs=new double[params.length];//所有参数的计算结果

                //计算公共部分的值,1/b*1/c*1/d*1/e，除了第1个参数外的所有参数的倒数相乘
                double pub=1;
                pcs[0]=params[0].calc();//计算a的值
                for(int i=1;i<params.length;i++){
                    pcs[i]=params[i].calc();
                    pub*=(1/pcs[i]);//所有（除第1个）参数的倒数相乘
                }

                //从后面的参数开始计算
                for(int i=0;i<params.length;i++){
                    Function param=params[i];
                    if(param.isDx(dx)){
                        val+=this.prtGradEx(param,dx,
                                i==0?
                                        pub    //1/b*1/c*1/d*1/e
                                        :
                                        (-pcs[0])*(pub/pcs[i])  //-a*(1/b*1/c*1/d*1/e)/当前参数值
                        );
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
            if(i==0){
                calcVal=this.getParams()[i].calc();
            }else{
                calcVal/=this.getParams()[i].calc();
            }
        }
        return calcVal;
    }

}
