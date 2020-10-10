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
     * @param kernelSize
     * @param strides
     * @return
     */
    public static ThreeDimShape getConvNumValid(ThreeDimShape inputShape,
                                                int kernelSize,
                                                int strides){
        ThreeDimShape threeDimShape =new ThreeDimShape(
                getConvNumValid(inputShape.getX(),kernelSize,strides),
                getConvNumValid(inputShape.getY(),kernelSize,strides),
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
     * @param kernelSize
     * @param strides
     * @return
     */
    public static ThreeDimShape getConvNumSame(ThreeDimShape inputShape,
                                               int kernelSize,
                                               int strides){
        ThreeDimShape threeDimShape =new ThreeDimShape(
                getConvNumSame(inputShape.getX(),kernelSize,strides),
                getConvNumSame(inputShape.getY(),kernelSize,strides),
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
    public static MultiDim expand(MultiDim data, ThreeDimShape destShape){
        MultiDim ret=null;
        ThreeDimShape srcShape=(ThreeDimShape)data.getShape();

        if(srcShape.equals(destShape)){
            return data;
        }else{
            ret=new MultiDim(destShape);
            double[][][] retData=(double[][][])ret.getData();
            double[][][] srcData=(double[][][])data.getData();

            //计算填充的行开始、行长度、列开始、列长度位置
            int rowStart,rowLen,colStart,colLen;
            //如果目标形状的行比原形状的行小
            if(srcShape.getX()>destShape.getX()){
                rowStart=0;
                rowLen=destShape.getX();
            }else{
                rowStart=(destShape.getX()-srcShape.getX())/2;
                rowLen=srcShape.getX();
            }
            //如果目标形状的列比原形状的列小
            if(srcShape.getY()>destShape.getY()){
                colStart=0;
                colLen=destShape.getY();
            }else{
                colStart=(destShape.getY()-srcShape.getY())/2;
                colLen=srcShape.getY();
            }

            //循环X，Y轴，填充数据到扩展后的数组
            for(int x=rowStart;x<rowStart+rowLen;x++){
                for(int y=colStart;y<colStart+colLen;y++){
                    retData[x][y]=srcData[x-rowStart][y-colStart];
                }
            }

            return ret;
        }
    }

    /**
     * 计算卷积
     * @param data 输入数据
     * @param w 过滤器
     * @param b 偏置
     * @param strides 步长
     * @return
     */
    public static MultiDim conv(MultiDim data,MultiDim w,MultiDim b,float keepProb,int strides,ThreeDimShape outShape){
        MultiDim ret=new MultiDim(outShape);
        double[][][] retData=(double[][][])ret.getData();
        double[][][] dataData=(double[][][])data.getData();
        double[][][][] wData=(double[][][][])w.getData();
        double[] bData=b!=null?(double[])b.getData():null;

        ThreeDimShape dataShape=(ThreeDimShape)data.getShape();
        FourDimShape wShape=(FourDimShape)w.getShape();

        //循环所有过滤器
        for(int i = 0; i<wShape.getW(); i++){
            //计算卷积
            retData[i]=conv(dataData,wData[i],bData!=null?bData[i]:1,keepProb,strides);
        }
        return ret;
    }

    /**
     * 计算3维数据的卷积
     * @param data
     * @param w
     * @param strides
     * @return
     */
    public static double[][] conv(double[][][] data,double[][][] w,double b,float keepProb,int strides){
        int width=(data.length-w.length)/strides+1;//计算卷积后的宽度
        int height=(data[0].length-w[0].length)/strides+1;//计算卷积后的高度

        double[][] ret=new double[width][height];

        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                ret[x][y]=(inner(data,x*strides,y*strides,w)+b)/keepProb;
            }
        }
        return ret;
    }

    /**
     * 计算内积
     * @param data 数据
     * @param xOffset x轴偏移
     * @param yOffset y轴偏移
     * @param w 权重
     * @return
     */
    public static double inner(double[][][] data,int xOffset,int yOffset,double[][][] w){
        double ret=0;
        for(int x=0;x<w.length;x++){
            for(int y=0;y<w[x].length;y++){
                for(int z=0;z<w[x][y].length;z++){
                    ret+=data[x+xOffset][y+yOffset][z]*w[x][y][z];
                }
            }
        }
        return ret;
    }
}