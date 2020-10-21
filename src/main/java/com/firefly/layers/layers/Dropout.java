package com.firefly.layers.layers;

import com.firefly.layers.core.Layer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.data.ThreeDimShape;
import com.firefly.math.Binomial;

/**
 * Dropout层
 */
public class Dropout implements Layer {
    private static float KEEP_PROB_DEFAULT=1.0F;
    private Shape inputShape;//输入形状
    private Shape unitShape;//输出形状
    private float keepProb=KEEP_PROB_DEFAULT;//节点保留概率，dropout功能

    public Dropout(float keepProb){
        this.keepProb=keepProb;
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
        do{
            double val=(double)input.getVal(index);
            out.setVal(index,val/keepProb);
        }while (index.next());
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {

    }

    @Override
    public void addBackUpdateParamPrtGrad(MultiDim input, MultiDim targetVal, MultiDim outFrontLayerPrtGrad, MultiDim backLayerPrtGrad) {
        MultiDim binomial= Binomial.binomialOfInt(keepProb,input.getShape());//二项分布
        ShapeIndex index=new ShapeIndex(input.getShape());
        do{
            int b=(int)binomial.getVal(index);
            if(b!=0){
                double outVal=(double)outFrontLayerPrtGrad.getVal(index);
                double val=(double)backLayerPrtGrad.getVal(index);

                //累加梯度
                outFrontLayerPrtGrad.setVal(index,outVal+val*b);
            }
        }while(index.next());
    }

    @Override
    public void flushBackUpdateParamPrtGrad() {

    }
}
