package com.firefly.layers.layers;

import com.firefly.layers.core.Layer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;

/**
 * Softmax层
 */
public class Softmax implements Layer {
    private Shape inputShape;//输入形状
    private Shape unitShape;//输出形状

    public Softmax(){
    }

    @Override
    public Shape getInputShape() {
        return inputShape;
    }

    @Override
    public void setInputShape(Shape inputShape) {
        this.inputShape=inputShape;
        this.unitShape=inputShape;
    }

    @Override
    public Shape getUnitShape() {
        return unitShape;
    }

    @Override
    public void setUnitShape(Shape unitShape) {
        this.unitShape=unitShape;
    }

    @Override
    public MultiDim getW() {
        return null;
    }

    @Override
    public void setW(MultiDim w) {

    }

    @Override
    public MultiDim getB() {
        return null;
    }

    @Override
    public void setB(MultiDim b) {

    }

    @Override
    public void init() {

    }

    @Override
    public void calc(MultiDim input, MultiDim out) {
        ShapeIndex index=new ShapeIndex(inputShape);
        //求所有输入变量通过exp函数计算求和
        double sum=sumExp(input);
        do{
            double val=(double)input.getVal(index);
            val=Math.exp(val)/sum;
            out.setVal(index,val);
        }while (index.next());
    }

    /**
     * 求所有输入变量通过exp函数计算求和
     * @param input
     * @return
     */
    private double sumExp(MultiDim input){
        double ret=0;
        ShapeIndex index=new ShapeIndex(input.getShape());
        do{
            double val=(double)input.getVal(index);
            ret+=Math.exp(val);
        }while (index.next());
        return ret;
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {

    }

    @Override
    public void addBackUpdateParamPrtGrad(MultiDim input, MultiDim targetVal, MultiDim outFrontLayerPrtGrad, MultiDim backLayerPrtGrad) {
        MultiDim out=new MultiDim(inputShape);
        calc(input,out);

        ShapeIndex index=new ShapeIndex(input.getShape());
        do{
            double outVal=(double)outFrontLayerPrtGrad.getVal(index);

            //计算单个softmax元素的梯度
            double val=calcOutFrontLayerPrtGradSigle(index,out,backLayerPrtGrad);

            //累加梯度
            outFrontLayerPrtGrad.setVal(index,outVal+val);
        }while(index.next());
    }

    public double calcOutFrontLayerPrtGradSigle(ShapeIndex j,MultiDim out,MultiDim backLayerPrtGrad){
        double ret=0;
        ShapeIndex i=new ShapeIndex(backLayerPrtGrad.getShape());
        do{
            double partGrad=(double)backLayerPrtGrad.getVal(i);
            if(partGrad!=0){
                double ai=(double)out.getVal(i);

                //如果两个下标相同
                if(i.equals(j)){
                    ret+=partGrad*ai*(1-ai);
                }else{
                    double aj=(double)out.getVal(j);
                    ret+=partGrad*(-ai*aj);
                }
            }
        }while(i.next());
        return ret;
    }

    @Override
    public void flushBackUpdateParamPrtGrad() {

    }
}
