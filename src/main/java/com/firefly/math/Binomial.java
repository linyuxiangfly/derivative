package com.firefly.math;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;

/**
 * 二项分布
 */
public class Binomial {
    public static MultiDim binomial(Class<?> type,float keepProb, Shape shape){
        MultiDim ret=new MultiDim(type,shape);
        ShapeIndex index=new ShapeIndex(shape);
        do{
            ret.setVal(index,Math.random()<=keepProb?1:0);
        }while(index.next());
        return ret;
    }

    public static MultiDim binomialOfInt(float keepProb, Shape shape){
        return binomial(Integer.TYPE,keepProb,shape);
    }

    public static MultiDim binomialOfFloat(float keepProb, Shape shape){
        return binomial(Float.TYPE,keepProb,shape);
    }

    public static MultiDim binomialOfBool(float keepProb, Shape shape){
        MultiDim ret=new MultiDim(Boolean.TYPE,shape);
        ShapeIndex index=new ShapeIndex(shape);
        do{
            ret.setVal(index,Math.random()<=keepProb);
        }while(index.next());
        return ret;
    }

    /**
     * 返回布尔类型的二项分布
     * @param keepProb 节点保留的概率
     * @param num 节点数
     * @return
     */
    public static boolean[] binomialOfBool(float keepProb,int num){
        boolean[] ret=new boolean[num];
        for(int i=0;i<ret.length;i++){
            ret[i]=Math.random()<=keepProb;
        }
        return ret;
    }

    /**
     * 返回整型的二项分布
     * @param keepProb 节点保留的概率
     * @param num 节点数
     * @return
     */
    public static int[] binomialOfInt(float keepProb,int num){
        int[] ret=new int[num];
        for(int i=0;i<ret.length;i++){
            ret[i]=Math.random()<=keepProb?1:0;
        }
        return ret;
    }


    /**
     * 返回浮点型的二项分布
     * @param keepProb 节点保留的概率
     * @param num 节点数
     * @return
     */
    public static float[] binomialOfFloat(float keepProb,int num){
        float[] ret=new float[num];
        for(int i=0;i<ret.length;i++){
            ret[i]=Math.random()<=keepProb?1:0;
        }
        return ret;
    }
}
