package com.firefly.derivative.core;

/**
 * 多目运算符
 */
public class OperationMultiple implements Function {
    private Function[] params;

    public OperationMultiple(){

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
