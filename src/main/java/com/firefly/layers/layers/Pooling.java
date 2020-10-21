package com.firefly.layers.layers;

import com.firefly.layers.core.Layer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.data.ThreeDimShape;
import com.firefly.layers.enums.PollingType;

/**
 * 池化层
 */
public class Pooling implements Layer {
    private ThreeDimShape inputShape;//输入形状
    private PollingType pollingType;
    private Shape unitShape;//输出单元数
    private MultiDim gradient;//当前输入的数据对应的梯度
    private int kernelWidth;//池化核宽度
    private int kernelHeight;//池化核高度

    public Pooling(PollingType pollingType, int kernelSize){
        this(pollingType,kernelSize,kernelSize);
    }

    public Pooling(PollingType pollingType, int kernelWidth, int kernelHeight){
        this(null,pollingType,kernelWidth,kernelHeight);
    }

    public Pooling(ThreeDimShape inputShape, PollingType pollingType, int kernelWidth, int kernelHeight){
        this.inputShape=inputShape;
        this.pollingType=pollingType;
        this.kernelWidth=kernelWidth;
        this.kernelHeight=kernelHeight;
    }

    /**
     * 计算输入的形状
     * @param inputShape
     * @param kernelWidth
     * @param kernelHeight
     * @return
     */
    private Shape calcOutShape(ThreeDimShape inputShape, int kernelWidth, int kernelHeight){
        int outWidth=inputShape.getX()/kernelWidth;
        int outHeight=inputShape.getY()/kernelHeight;

        return new Shape(new int[]{outWidth,outHeight,inputShape.getZ()});
    }

    /**
     * 设置3维输出值
     * @param val
     * @param index
     */
    private void setThreeDimOutVal(MultiDim md, ShapeIndex index,double val){
        md.setVal(index,val);
    }

    public Shape getInputShape() {
        return inputShape;
    }

    public void setInputShape(Shape inputShape) {
        this.inputShape = (ThreeDimShape) inputShape;
        this.unitShape=calcOutShape(this.inputShape,kernelWidth,kernelHeight);
    }

    public Shape getUnitShape() {
        return unitShape;
    }

    public void setUnitShape(Shape unitShape) {
        this.unitShape = unitShape;
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
        //计算输入的形状
        this.unitShape=calcOutShape(inputShape,kernelWidth,kernelHeight);
        this.gradient=new MultiDim(inputShape);
    }

    @Override
    public void calc(MultiDim input, MultiDim out) {
        int outWidth=inputShape.getX()/kernelWidth;
        int outHeight=inputShape.getY()/kernelHeight;

        for(int z=0;z<inputShape.getZ();z++){
            for(int x=0;x<outWidth;x++){
                for(int y=0;y<outHeight;y++){
                    if(pollingType==PollingType.max){
                        max(input,out,gradient,x,y,z,x*kernelWidth,kernelWidth,y*kernelHeight,kernelHeight,z);
                    }else{
                        avg(input,out,gradient,x,y,z,x*kernelWidth,kernelWidth,y*kernelHeight,kernelHeight,z);
                    }
                }
            }
        }
    }

    /**
     * 设置梯度的值
     * @param gradient
     * @param xStart
     * @param xLen
     * @param yStart
     * @param yLen
     * @param zStart
     * @param zLen
     */
    private void setGradientVal(MultiDim gradient,int xStart,int xLen,int yStart,int yLen,int zStart,int zLen,double val){
        ShapeIndex index=new ShapeIndex(gradient.getShape());
        for(int x=xStart;x<xStart+xLen;x++){
            for(int y=yStart;y<yStart+yLen;y++){
                for(int z=zStart;z<zStart+zLen;z++){
                    index.setDimIndexVal(0,x);
                    index.setDimIndexVal(1,y);
                    index.setDimIndexVal(2,z);
                    gradient.setVal(index,val);
                }
            }
        }
    }

    /**
     * 计算最大值和梯度
     * @param input
     * @param out
     * @param gradient
     * @param px
     * @param py
     * @param pz
     * @param xStart
     * @param xLen
     * @param yStart
     * @param yLen
     * @param z
     */
    private void max(MultiDim input,MultiDim out,MultiDim gradient,int px,int py,int pz,int xStart,int xLen,int yStart,int yLen,int z){
        //求最大值或平均值
        int maxX=xStart;
        int maxY=yStart;

        ShapeIndex index=new ShapeIndex(inputShape);
        ShapeIndex outIndex=new ShapeIndex(unitShape);

        index.setDimIndexVal(ThreeDimShape.X,xStart);
        index.setDimIndexVal(ThreeDimShape.Y,yStart);
        index.setDimIndexVal(ThreeDimShape.Z,z);
        double maxVal=(double)input.getVal(index);

        for(int xx=xStart;xx<xStart+xLen;xx++){
            for(int yy=yStart;yy<yStart+yLen;yy++){
                index.setDimIndexVal(ThreeDimShape.X,xx);
                index.setDimIndexVal(ThreeDimShape.Y,yy);
                double tempVal=(double)input.getVal(index);
                if(tempVal>maxVal){
                    maxX=xx;
                    maxY=yy;
                    maxVal=tempVal;
                }
            }
        }

        //记录梯度值
        setGradientVal(gradient,xStart,xLen,yStart,yLen,z,1,0.0);//置零

        //设置最大值的点梯度为1
        index.setDimIndexVal(ThreeDimShape.X,maxX);
        index.setDimIndexVal(ThreeDimShape.Y,maxY);
        index.setDimIndexVal(ThreeDimShape.Z,z);
        gradient.setVal(index,1.0);

        //设置最大值
        outIndex.setDimIndexVal(ThreeDimShape.X,px);
        outIndex.setDimIndexVal(ThreeDimShape.Y,py);
        outIndex.setDimIndexVal(ThreeDimShape.Z,pz);
        setThreeDimOutVal(out,outIndex,maxVal);
    }

    /**
     * 计算平均值和梯度
     * @param input
     * @param out
     * @param gradient
     * @param px
     * @param py
     * @param pz
     * @param xStart
     * @param xLen
     * @param yStart
     * @param yLen
     * @param z
     */
    private void avg(MultiDim input,MultiDim out,MultiDim gradient,int px,int py,int pz,int xStart,int xLen,int yStart,int yLen,int z){
        double sum=0;
        double num=xLen*yLen;//计算数量
        ShapeIndex index=new ShapeIndex(inputShape);
        ShapeIndex outIndex=new ShapeIndex(unitShape);

        index.setDimIndexVal(ThreeDimShape.X,xStart);
        index.setDimIndexVal(ThreeDimShape.Y,yStart);
        index.setDimIndexVal(ThreeDimShape.Z,z);

        for(int xx=xStart;xx<xStart+xLen;xx++){
            for(int yy=yStart;yy<yStart+yLen;yy++){
                index.setDimIndexVal(ThreeDimShape.X,xx);
                index.setDimIndexVal(ThreeDimShape.Y,yy);
                double tempVal=(double)input.getVal(index);
                sum+=tempVal;
            }
        }

        //记录梯度值
        setGradientVal(gradient,xStart,xLen,yStart,yLen,z,1,1.0/num);//设置梯度

        //设置最大值
        outIndex.setDimIndexVal(ThreeDimShape.X,px);
        outIndex.setDimIndexVal(ThreeDimShape.Y,py);
        outIndex.setDimIndexVal(ThreeDimShape.Z,pz);
        //设置输出值
        setThreeDimOutVal(out,outIndex,sum/num);
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {

    }

    @Override
    public void addBackUpdateParamPrtGrad(MultiDim input,MultiDim targetVal,MultiDim outFrontLayerPrtGrad,MultiDim backLayerPrtGrad) {
        //累计输入参数的更新值
        if(outFrontLayerPrtGrad!=null){
            setInputPrtGrad(gradient,outFrontLayerPrtGrad,backLayerPrtGrad);

            //计算输入值的更新梯度
//            for(int i=0;i<currentPrtGradVal.length;i++){
//                currentPrtGradVal[i]+=cpt[i];
//            }
        }
    }

    private void setInputPrtGrad(MultiDim gradient,MultiDim outFrontLayerPrtGrad,MultiDim backLayerPrtGrad){
        ShapeIndex index=new ShapeIndex(unitShape);
        do{
            double val;
            if(backLayerPrtGrad.getShape().getDims().length==1){
                val=(double)backLayerPrtGrad.getVal(new int[]{index.getMultDim2OneDimIndex()});
            }else{
                val=(double)backLayerPrtGrad.getVal(index);
            }
            addGradientVal(
                    gradient,
                    outFrontLayerPrtGrad,
                    index.getDimIndexVal(ThreeDimShape.X)*kernelWidth,
                    kernelWidth,
                    index.getDimIndexVal(ThreeDimShape.Y)*kernelHeight,
                    kernelHeight,
                    index.getDimIndexVal(ThreeDimShape.Z),
                    1,
                    val
                    );
        }while (index.next());
    }

    private void addGradientVal(MultiDim gradient,MultiDim outFrontLayerPrtGrad,int xStart,int xLen,int yStart,int yLen,int zStart,int zLen,double val){
        if(val!=0){
            ShapeIndex index=new ShapeIndex(gradient.getShape());
            for(int x=xStart;x<xStart+xLen;x++){
                for(int y=yStart;y<yStart+yLen;y++){
                    for(int z=zStart;z<zStart+zLen;z++){
                        index.setDimIndexVal(0,x);
                        index.setDimIndexVal(1,y);
                        index.setDimIndexVal(2,z);

                        double v=(double)gradient.getVal(index);
                        if(v!=0){
                            double ov=(double)outFrontLayerPrtGrad.getVal(index);
                            //更新输出梯度
                            outFrontLayerPrtGrad.setVal(index,ov+v*val);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void flushBackUpdateParamPrtGrad() {

    }
}
