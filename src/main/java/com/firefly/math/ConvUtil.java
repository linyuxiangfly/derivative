package com.firefly.math;

import com.firefly.layers.data.FourDimShape;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ThreeDimShape;

public class ConvUtil {
    /**
     * 获取卷积滑窗的长度，不填充的
     * @param inputNum
     * @param kernelNum
     * @param strides
     * @return
     */
    public static int getConvNumValid(int inputNum,int kernelNum,int strides){
        int m=(inputNum-kernelNum)%strides;//计算剩余多少长度不能使用
        return inputNum-m;
    }

    /**
     * 获取卷积滑窗的形状，不填充的
     * @param inputShape
     * @param kernelWidth
     * @param kernelHeight
     * @param strides
     * @return
     */
    public static ThreeDimShape getConvNumValid(ThreeDimShape inputShape,
                                                int kernelWidth,int kernelHeight,
                                                int strides){
        ThreeDimShape threeDimShape =new ThreeDimShape(
                getConvNumValid(inputShape.getX(),kernelWidth,strides),
                getConvNumValid(inputShape.getY(),kernelHeight,strides),
                inputShape.getZ()
        );
        return threeDimShape;
    }

    /**
     * 获取卷积滑窗的长度，当滑动步长大于1时：填充数=K-I%S（K:卷积核边长，I：输入图像边长，S：滑动步长）
     * 滑动步长为1时，填充数是卷积核边长减1，eg:5*5的图用3*3的核，步长为1时same填充之后是7*7
     * @param inputNum
     * @param kernelNum
     * @param strides
     * @return
     */
    public static int getConvNumSame(int inputNum,int kernelNum,int strides){
        if(strides>1){
            return inputNum+(kernelNum-inputNum%strides)*2;
        }else{
            return inputNum+(kernelNum-1)*2;
        }
    }

    /**
     * 获取卷积滑窗的形状，不填充的
     * @param inputShape
     * @param kernelWidth
     * @param kernelHeight
     * @param strides
     * @return
     */
    public static ThreeDimShape getConvNumSame(ThreeDimShape inputShape,
                                               int kernelWidth,int kernelHeight,
                                               int strides){
        ThreeDimShape threeDimShape =new ThreeDimShape(
                getConvNumSame(inputShape.getX(),kernelWidth,strides),
                getConvNumSame(inputShape.getY(),kernelHeight,strides),
                inputShape.getZ()
        );
        return threeDimShape;
    }

    /**
     * 扩大数据，将数据居中填充到扩大或缩小后的数组
     * @param data
     * @param destShape
     * @return
     */
//    public static MultiDim expand(MultiDim data, ThreeDimShape destShape){
//        MultiDim ret=null;
//        Shape srcShape=data.getShape();
//
//        if(srcShape.equals(destShape)){
//            return data;
//        }else{
//            ret=new MultiDim(destShape);
//            double[][][] retData=(double[][][])ret.getData();
//            double[][][] srcData=(double[][][])data.getData();
//
//            //计算填充的行开始、行长度、列开始、列长度位置
//            int rowStart,rowLen,colStart,colLen;
//            //如果目标形状的行比原形状的行小
//            if(srcShape.getDim(ThreeDimShape.X)>destShape.getX()){
//                rowStart=0;
//                rowLen=destShape.getX();
//            }else{
//                rowStart=(destShape.getX()-srcShape.getDim(ThreeDimShape.X))/2;
//                rowLen=srcShape.getDim(ThreeDimShape.X);
//            }
//            //如果目标形状的列比原形状的列小
//            if(srcShape.getDim(ThreeDimShape.Y)>destShape.getY()){
//                colStart=0;
//                colLen=destShape.getY();
//            }else{
//                colStart=(destShape.getY()-srcShape.getDim(ThreeDimShape.Y))/2;
//                colLen=srcShape.getDim(ThreeDimShape.Y);
//            }
//
//            //循环X，Y轴，填充数据到扩展后的数组
//            for(int x=rowStart;x<rowStart+rowLen;x++){
//                for(int y=colStart;y<colStart+colLen;y++){
//                    retData[x][y]=srcData[x-rowStart][y-colStart];
//                }
//            }
//
//            return ret;
//        }
//    }

    /**
     * 计算卷积
     * @param data 输入数据
     * @param w 过滤器
     * @param b 偏置
     * @param strides 步长
     * @return
     */
    public static MultiDim conv(MultiDim data,ThreeDimShape inputShapeExpand,MultiDim w,MultiDim b,int strides,ThreeDimShape outShape){
        MultiDim ret=new MultiDim(outShape);
        double[][][] retData=(double[][][])ret.getData();
        double[][][] dataData=(double[][][])data.getData();
        double[][][][] wData=(double[][][][])w.getData();
        double[] bData=b!=null?(double[])b.getData():null;

        //计算卷积
        conv(dataData,inputShapeExpand,wData,bData,strides,retData);
        return ret;
    }

    /**
     * 计算3维数据的卷积
     * @param data
     * @param w
     * @param strides
     * @return
     */
    public static void conv(double[][][] data,ThreeDimShape inputShapeExpand,double[][][][] w,double[] b,int strides,double[][][] outData){
        int width=(inputShapeExpand.getX()-w.length)/strides+1;//计算卷积后的宽度
        int height=(inputShapeExpand.getY()-w[0].length)/strides+1;//计算卷积后的高度

        //计算填充的行开始、行长度、列开始、列长度位置
        int rowStart,colStart;
        //如果目标形状的行比原形状的行小
        if(data.length>inputShapeExpand.getX()){
            rowStart=0;
        }else{
            rowStart=(inputShapeExpand.getX()-data.length)/2;
        }
        //如果目标形状的列比原形状的列小
        if(data[0].length>inputShapeExpand.getY()){
            colStart=0;
        }else{
            colStart=(inputShapeExpand.getY()-data[0].length)/2;
        }

        for(int z=0;z<outData[0][0].length;z++){
            for(int x=0;x<width;x++){
                for(int y=0;y<height;y++){
                    outData[x][y][z]=(inner(data,x*strides-rowStart,y*strides-colStart,z,w)+b[z]);
                }

            }
        }
    }

    /**
     * 计算内积
     * @param data 数据
     * @param xOffset x轴偏移
     * @param yOffset y轴偏移
     * @param weight 权重
     * @return
     */
    public static double inner(double[][][] data,int xOffset,int yOffset,int filter,double[][][][] weight){
        double ret=0;
        for(int x=0;x<weight.length;x++){
            for(int y=0;y<weight[x].length;y++){
                if(
                        (x+xOffset>=0 && x+xOffset<data.length) &&
                                (y+yOffset>=0 && y+yOffset<data[0].length)
                ){
                    for(int z=0;z<weight[x][y].length;z++){
                        ret+=data[x+xOffset][y+yOffset][z]*weight[x][y][z][filter];
                    }
                }
            }
        }
        return ret;
    }
}
