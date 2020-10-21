package com.firefly.layers.layers;

import com.firefly.layers.core.Layer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.math.Binomial;

/**
 * 缩放层
 */
public class Zoom implements Layer {
    private Shape inputShape;//输入形状
    private Shape unitShape;//输出形状
    private boolean hasInputMinMax;//是否有输入的最大值和最小值
    private double inputMin;
    private double inputMax;
    private double outMin;
    private double outMax;
    private double scaling;//缩放比例

    public Zoom(double outMin,double outMax){
        this.outMin=outMin;
        this.outMax=outMax;
        hasInputMinMax=false;
    }

    public Zoom(double inputMin,double inputMax,double outMin,double outMax){
        this.inputMin=inputMin;
        this.inputMax=inputMax;
        this.outMin=outMin;
        this.outMax=outMax;
        hasInputMinMax=true;
        scaling=calcScaling(inputMin,inputMax,outMin,outMax);
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
        //如果没有输入的最大值和最小值
        if(!hasInputMinMax){
            calcInputMaxMinVal(input);
            scaling=calcScaling(inputMin,inputMax,outMin,outMax);
        }

        do{
            double val=(double)input.getVal(index);
            out.setVal(index,calcVal(val,scaling,inputMin,outMin));
        }while (index.next());
    }

    private double calcVal(double val,double scaling,double inputMin,double outMin){
        return (val-inputMin)*scaling+outMin;
    }

    private double calcScaling(double inputMin,double inputMax,double outMin,double outMax){
        //计算缩放比例
        return (outMax-outMin)/(inputMax-inputMin);
    }

    /**
     * 计算最大值和最小值
     * @param input
     */
    private void calcInputMaxMinVal(MultiDim input){
        ShapeIndex index=new ShapeIndex(inputShape);
        inputMin=(double)input.getVal(index);
        inputMax=inputMin;
        do{
            double val=(double)input.getVal(index);
            if(val<inputMin){
                inputMin=val;
            }
            if(val>inputMax){
                inputMax=val;
            }
        }while (index.next());
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {

    }

    @Override
    public void addBackUpdateParamPrtGrad(MultiDim input, MultiDim targetVal, MultiDim outFrontLayerPrtGrad, MultiDim backLayerPrtGrad) {
        ShapeIndex index=new ShapeIndex(input.getShape());
        do{
            double outVal=(double)outFrontLayerPrtGrad.getVal(index);
            double val=(double)backLayerPrtGrad.getVal(index);

            //累加梯度
            outFrontLayerPrtGrad.setVal(index,outVal+val*scaling);
        }while(index.next());
    }

    @Override
    public void flushBackUpdateParamPrtGrad() {

    }
}
