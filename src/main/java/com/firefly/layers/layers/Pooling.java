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
    private Shape unitRealShape;//输入单元真实形状
    private MultiDim gradient;//当前输入的数据对应的梯度
    private int kernelWidth;//池化核宽度
    private int kernelHeight;//池化核高度
    private boolean outOneDim;//多维转为1维输出

    public Pooling(PollingType pollingType, int kernelWidth, int kernelHeight, boolean outOneDim){
        this(null,pollingType,kernelWidth,kernelHeight,outOneDim);
    }

    public Pooling(ThreeDimShape inputShape, PollingType pollingType, int kernelWidth, int kernelHeight, boolean outOneDim){
        this.inputShape=inputShape;
        this.pollingType=pollingType;
        this.kernelWidth=kernelWidth;
        this.kernelHeight=kernelHeight;
        this.outOneDim=outOneDim;
    }

    /**
     * 计算输入的形状
     * @param inputShape
     * @param kernelWidth
     * @param kernelHeight
     * @param outOneDim
     * @return
     */
    private Shape calcOutShape(ThreeDimShape inputShape, int kernelWidth, int kernelHeight, boolean outOneDim){
        int outWidth=inputShape.getY()/kernelWidth;
        int outHeight=inputShape.getZ()/kernelHeight;

        //如果输出1维
        if(outOneDim){
            return new Shape(new int[]{inputShape.getX()*outWidth*outHeight});
        }else{
            return new Shape(new int[]{inputShape.getX(),outWidth,outHeight});
        }
    }

    /**
     * 设置1维输出值
     * @param val
     * @param index
     */
    private void setOneDimOutVal(MultiDim md, ShapeIndex index,double val){
        md.setVal(new int[]{index.getMultDim2OneDimIndex()},val);
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
        this.unitShape=calcOutShape(this.inputShape,kernelWidth,kernelHeight,outOneDim);
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
        this.unitShape=calcOutShape(inputShape,kernelWidth,kernelHeight,outOneDim);
        this.unitRealShape=calcOutShape(inputShape,kernelWidth,kernelHeight,false);
        this.gradient=new MultiDim(inputShape);
    }

    @Override
    public void calc(MultiDim input, MultiDim out) {
        int outWidth=inputShape.getY()/kernelWidth;
        int outHeight=inputShape.getZ()/kernelHeight;

        for(int x=0;x<inputShape.getX();x++){
            for(int y=0;y<outWidth;y++){
                for(int z=0;z<outHeight;z++){
                    if(pollingType==PollingType.max){
                        max(input,out,gradient,x,y,z,x,y*kernelWidth,kernelWidth,z*kernelHeight,kernelHeight,outOneDim);
                    }else{
                        avg(input,out,gradient,x,y,z,x,y*kernelWidth,kernelWidth,z*kernelHeight,kernelHeight,outOneDim);
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
     * @param x
     * @param yStart
     * @param yLen
     * @param zStart
     * @param zLen
     * @param outOneDim
     */
    private void max(MultiDim input,MultiDim out,MultiDim gradient,int px,int py,int pz,int x,int yStart,int yLen,int zStart,int zLen,boolean outOneDim){
        //求最大值或平均值
        int maxY=yStart;
        int maxZ=zStart;

        ShapeIndex index=new ShapeIndex(inputShape);
        ShapeIndex outIndex=new ShapeIndex(unitRealShape);

        index.setDimIndexVal(inputShape.X,x);
        index.setDimIndexVal(inputShape.Y,yStart);
        index.setDimIndexVal(inputShape.Z,zStart);
        double maxVal=(double)input.getVal(index);

        for(int yy=yStart;yy<yStart+yLen;yy++){
            for(int zz=zStart;zz<zStart+zLen;zz++){
                index.setDimIndexVal(inputShape.Y,yy);
                index.setDimIndexVal(inputShape.Z,zz);
                double tempVal=(double)input.getVal(index);
                if(tempVal>maxVal){
                    maxY=yy;
                    maxZ=zz;
                    maxVal=tempVal;
                }
            }
        }

        //记录梯度值
        setGradientVal(gradient,x,1,yStart,yLen,zStart,zLen,0.0);//置零

        //设置最大值的点梯度为1
        index.setDimIndexVal(inputShape.X,x);
        index.setDimIndexVal(inputShape.Y,maxY);
        index.setDimIndexVal(inputShape.Z,maxZ);
        gradient.setVal(index,1.0);

        //设置最大值
        outIndex.setDimIndexVal(inputShape.X,px);
        outIndex.setDimIndexVal(inputShape.Y,py);
        outIndex.setDimIndexVal(inputShape.Z,pz);
        if(outOneDim){
            setOneDimOutVal(out,outIndex,maxVal);
        }else{
            setThreeDimOutVal(out,outIndex,maxVal);
        }
    }

    /**
     * 计算平均值和梯度
     * @param input
     * @param out
     * @param gradient
     * @param px
     * @param py
     * @param pz
     * @param x
     * @param yStart
     * @param yLen
     * @param zStart
     * @param zLen
     * @param outOneDim
     */
    private void avg(MultiDim input,MultiDim out,MultiDim gradient,int px,int py,int pz,int x,int yStart,int yLen,int zStart,int zLen,boolean outOneDim){
        double sum=0;
        double num=yLen*zLen;//计算数量
        ShapeIndex index=new ShapeIndex(inputShape);

        index.setDimIndexVal(inputShape.X,x);
        index.setDimIndexVal(inputShape.Y,yStart);
        index.setDimIndexVal(inputShape.Z,zStart);

        for(int yy=yStart;yy<yStart+yLen;yy++){
            for(int zz=zStart;zz<zStart+zLen;zz++){
                index.setDimIndexVal(inputShape.Y,yy);
                index.setDimIndexVal(inputShape.Z,zz);
                double tempVal=(double)input.getVal(index);
                sum+=tempVal;
            }
        }

        //记录梯度值
        setGradientVal(gradient,x,1,yStart,yLen,zStart,zLen,1.0/num);//设置梯度

        //设置最大值
        index.setDimIndexVal(inputShape.X,px);
        index.setDimIndexVal(inputShape.Y,py);
        index.setDimIndexVal(inputShape.Z,pz);
        if(outOneDim){
            setOneDimOutVal(out,index,sum/num);
        }else{
            setThreeDimOutVal(out,index,sum/num);
        }
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
        ShapeIndex index=new ShapeIndex(unitRealShape);
        do{
            double val=(double)backLayerPrtGrad.getVal(new int[]{index.getMultDim2OneDimIndex()});
            addGradientVal(
                    gradient,
                    outFrontLayerPrtGrad,
                    index.getDimIndexVal(ThreeDimShape.X),
                    1,
                    index.getDimIndexVal(ThreeDimShape.Y)*kernelWidth,
                    kernelWidth,
                    index.getDimIndexVal(ThreeDimShape.Z)*kernelHeight,
                    kernelHeight,
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
    public void flushBackUpdateParamPrtGrad(double rate) {

    }
}
