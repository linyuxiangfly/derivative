package com.firefly.derivative.core;

import com.firefly.derivative.operation.Const;

/**
 * 多目运算符
 */
public class OperationMultiple implements Function {
    private static final long serialVersionUID = 1L;

    private Function[] params;

    public OperationMultiple(){

    }

    public OperationMultiple(double[] params){
        this.setParams(params);
    }

    public OperationMultiple(Function[] params){
        this.params=params;
    }

    public Function[] getParams() {
        return params;
    }

    public void setParams(Function[] params) {
        this.params = params;
    }

    public void setParams(double[] params){
        if(params!=null && params.length>0){
            this.params=new Function[params.length];
            for(int i=0;i<this.params.length;i++){
                this.params[i]=new Const(params[i]);
            }
        }
    }

    /**
     * 如果参数中是否至少有一个要求偏导
     * @param dx
     * @return
     */
    private boolean isDxParams(Function dx){
        if(params!=null && params.length>0){
            for(Function func:params){
                if(func.isDx(dx)){
                    return true;
                }
            }
        }
        return false;
    }

    private Function oldDx;
    private boolean oldIsDx;
    @Override
    public boolean isDx(Function dx) {
        if(oldDx!=dx){
            oldDx=dx;
            oldIsDx=this==dx||isDxParams(dx);
        }
        return oldIsDx;
    }

    protected double prtGradEx(Function param,Function dx,double d){
//        return d*(param.isDx(dx)?param.prtGrad(dx):1);
        return d*param.prtGrad(dx);
    }

    @Override
    public double prtGrad(Function dx) {
        return 0;
    }

    @Override
    public double calc() {
        return 0;
    }
}
