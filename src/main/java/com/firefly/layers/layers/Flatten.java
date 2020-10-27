package com.firefly.layers.layers;

import com.firefly.layers.core.Layer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;

/**
 * 多维转1维层
 */
public class Flatten implements Layer {
    private static final long serialVersionUID = 1L;

    private Shape inputShape;//输入形状
    private Shape unitShape;//输出形状

    @Override
    public Shape getInputShape() {
        return inputShape;
    }

    @Override
    public void setInputShape(Shape inputShape) {
        this.inputShape=inputShape;
        //多维转1维的数量
        this.unitShape=new Shape(new int[]{inputShape.getNums()});
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
            out.setVal(new int[]{index.getMultDim2OneDimIndex()},val);
        }while (index.next());
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {

    }

    @Override
    public void addBackUpdateParamPrtGrad(MultiDim input, MultiDim targetVal, MultiDim outFrontLayerPrtGrad, MultiDim backLayerPrtGrad) {
        if(outFrontLayerPrtGrad!=null){
            ShapeIndex index=new ShapeIndex(input.getShape());
            do{
                double val=(double)backLayerPrtGrad.getVal(new int[]{index.getMultDim2OneDimIndex()});
                if(val!=0){
                    double outVal=(double)outFrontLayerPrtGrad.getVal(index);
                    //累加梯度
                    outFrontLayerPrtGrad.setVal(index,outVal+val);
                }
            }while(index.next());
        }
    }

    @Override
    public void flushBackUpdateParamPrtGrad(int batchSize) {

    }
}
