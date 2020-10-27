package com.firefly.derivative.core;

import com.firefly.derivative.operation.Const;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;

/**
 * 单目运算符
 */
public class OperationActivation extends OperationUnary {
    private static final long serialVersionUID = 1L;

    private MultiDim relations;//关联的参数

    public OperationActivation(){

    }

    public OperationActivation(double val){
        super(val);
    }

    public OperationActivation(Function val){
        super(val);
    }


    public OperationActivation(double val,MultiDim relations){
        super(val);
        setRelations(relations);
    }

    public OperationActivation(Function val,MultiDim relations){
        super(val);
        setRelations(relations);
    }

    public MultiDim getRelations() {
        return relations;
    }

    public void setRelations(MultiDim relations) {
        this.relations = relations;
    }

    /**
     * 如果参数中是否至少有一个要求偏导
     * @param dx
     * @return
     */
    private boolean isDxRelations(Function dx){
        if(relations!=null){
            ShapeIndex index=new ShapeIndex(relations.getShape());
            do{
                Function func=(Function)relations.getVal(index);
                if(func.isDx(dx)){
                    return true;
                }
            }while (index.next());
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

    public double prtGrad(Function dx,MultiDim targetVal){
        return 0;
    }

    @Override
    public double calc() {
        return 0;
    }
}
