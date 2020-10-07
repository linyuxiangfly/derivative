package com.firefly.math;

import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
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
     * @param filters
     * @param kernelSize
     * @param strides
     * @return
     */
    public static ThreeDimShape getConvNumValid(ThreeDimShape inputShape,
                                                int filters, ThreeDimShape kernelSize,
                                                int strides){
        ThreeDimShape threeDimShape =new ThreeDimShape(
                getConvNumValid(inputShape.getDims()[0],kernelSize.getDims()[0],strides),
                getConvNumValid(inputShape.getDims()[1],kernelSize.getDims()[1],strides),
                filters
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
            return kernelNum-inputNum%strides;
        }else{
            return kernelNum-1;
        }
    }

    /**
     * 获取卷积滑窗的形状，不填充的
     * @param inputShape
     * @param filters
     * @param kernelSize
     * @param strides
     * @return
     */
    public static ThreeDimShape getConvNumSame(ThreeDimShape inputShape,
                                               int filters, ThreeDimShape kernelSize,
                                               int strides){
        ThreeDimShape threeDimShape =new ThreeDimShape(
                getConvNumSame(inputShape.getDims()[0],kernelSize.getDims()[0],strides),
                getConvNumSame(inputShape.getDims()[1],kernelSize.getDims()[1],strides),
                filters
        );
        return threeDimShape;
    }

}
