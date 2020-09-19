package com.firefly.derivative.core;

import com.firefly.derivative.operation.Const;

/**
 * 单目运算符
 */
public class OperationActivation extends OperationUnary {
    private Function[] relations;//关联的参数
    private Function[] settings;//设置参数

    public OperationActivation(){

    }

    public OperationActivation(double val){
        super(val);
    }

    public OperationActivation(Function val){
        super(val);
    }

    public OperationActivation(double val,double[] relations){
        super(val);
        setRelations(relations);
    }

    public OperationActivation(double val,Function[] relations){
        super(val);
        setRelations(relations);
    }

    public OperationActivation(Function val,double[] relations){
        super(val);
        setRelations(relations);
    }

    public OperationActivation(Function val,Function[] relations){
        super(val);
        setRelations(relations);
    }

    public void setRelations(double[] vals){
        if(vals!=null && vals.length>0){
            this.relations=new Function[vals.length];
            for(int i=0;i<this.relations.length;i++){
                this.relations[i]=new Const(vals[i]);
            }
        }
    }

    public Function[] getRelations() {
        return relations;
    }

    public void setRelations(Function[] relations) {
        this.relations = relations;
    }

    public Function[] getSettings() {
        return settings;
    }

    public void setSettings(Function[] settings) {
        this.settings = settings;
    }

    /**
     * 如果参数中是否至少有一个要求偏导
     * @param dx
     * @return
     */
    private boolean isDxRelations(Function dx){
        if(relations!=null && relations.length>0){
            for(Function func:relations){
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
        oldIsDx=super.isDx(dx);
        if(!oldIsDx){
            if(oldDx!=dx){
                oldDx=dx;
                oldIsDx=isDxRelations(dx);
            }
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

    public double prtGrad(Function dx,double[] targetVal){
        return 0;
    }

    @Override
    public double calc() {
        return 0;
    }
}
