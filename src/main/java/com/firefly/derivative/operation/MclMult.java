package com.firefly.derivative.operation;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationMultiple;

/**
 * 乘法，多参数
 */
public class MclMult extends OperationMultiple {
    private static final long serialVersionUID = 1L;

    public MclMult(){

    }

    public MclMult(double[] params) {
        super(params);
    }

    public MclMult(Function[] params){
        super(params);
    }

    @Override
    public double prtGrad(Function dx) {
        double val=0;
        if(this==dx){
            val=1;
        }else{
            if(this.getParams()!=null && this.getParams().length>0){
                double count=mclParams(null);//将所有参数相乘
                for(Function param:this.getParams()){
                    if(param.isDx(dx)){
                        //mclParams装除了当前参数外的所有参数值相乘
                        val+=this.prtGradEx(param,dx,count/param.calc());
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
                calcVal*=this.getParams()[i].calc();
            }
        }
        return calcVal;
    }

    /**
     * 将所有参数相乘
     * @param except 除外
     * @return
     */
    private double mclParams(Function except){
        double ret=0;
        int i=0;
        for(Function param:this.getParams()){
            if(param!=except){
                if(i==0){
                    ret=param.calc();
                }else{
                    ret*=param.calc();
                }
                //如果发现零，则后面不用再乘了，因为再怎么乘都是0
                if(ret==0){
                    return ret;
                }
                i++;
            }
        }
        return ret;
    }

}
