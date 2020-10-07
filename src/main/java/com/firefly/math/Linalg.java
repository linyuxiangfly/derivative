package com.firefly.math;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;

/**
 * 线性代数函数库
 */
public class Linalg {
    /**
     * 矩阵加法
     * @param a
     * @param b
     * @return
     */
    public static double[] add(double[] a,double b){
        double[] ret=new double[a.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=a[i]+b;
        }
        return ret;
    }

    /**
     * 矩阵加法
     * @param a
     * @param b
     * @return
     */
    public static double[] add(double a,double[] b){
        return add(b,a);
    }

    public static double[] add(double[] a,double[] b){
        if(a.length==b.length){
            double[] ret=new double[a.length];
            for(int i=0;i<ret.length;i++){
                ret[i]=a[i]+b[i];
            }
            return ret;
        }else{
            throw new RuntimeException("The data shape is not correct");
        }
    }

    /**
     * 矩阵减法
     * @param a
     * @param b
     * @return
     */
    public static double[] sub(double[] a,double b){
        double[] ret=new double[a.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=a[i]-b;
        }
        return ret;
    }

    /**
     * 矩阵加法
     * @param a
     * @param b
     * @return
     */
    public static double[] sub(double a,double[] b){
        double[] ret=new double[b.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=a-b[i];
        }
        return ret;
    }

    public static double[] sub(double[] a,double[] b){
        if(a.length==b.length){
            double[] ret=new double[a.length];
            for(int i=0;i<ret.length;i++){
                ret[i]=a[i]-b[i];
            }
            return ret;
        }else{
            throw new RuntimeException("The data shape is not correct");
        }
    }

    public static MultiDim sub(MultiDim a,MultiDim b){
        MultiDim ret=null;
        if(a.getShape().equals(b.getShape())){
            ret=new MultiDim(a.getShape());
            ShapeIndex index=new ShapeIndex(a.getShape());
            do{
                ret.setVal(index,(double)a.getVal(index)-(double)b.getVal(index));
            }while(index.next());
        }
        return ret;
    }

    /**
     * 数组乘法
     * @param a
     * @param b
     * @return
     */
    public static double[] mcl(double[] a,double b){
        double[] ret=new double[a.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=a[i]*b;
        }
        return ret;
    }

    /**
     * 数组乘法
     * @param a
     * @param b
     * @return
     */
    public static double[] mcl(double a,double[] b){
        return mcl(b,a);
    }

    /**
     * 数组乘法
     * @param a
     * @param b
     * @return
     */
    public static double[] mcl(double[] a,double[] b){
        if(a.length==b.length){
            double[] ret=new double[a.length];
            for(int i=0;i<ret.length;i++){
                ret[i]=a[i]*b[i];
            }
            return ret;
        }else{
            throw new RuntimeException("The data shape is not correct");
        }
    }

    /**
     * 矩阵减法
     * @param a
     * @param b
     * @return
     */
    public static double[] div(double[] a,double b){
        double[] ret=new double[a.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=a[i]/b;
        }
        return ret;
    }

    /**
     * 矩阵加法
     * @param a
     * @param b
     * @return
     */
    public static double[] div(double a,double[] b){
        double[] ret=new double[b.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=a/b[i];
        }
        return ret;
    }

    public static double[] div(double[] a,double[] b){
        if(a.length==b.length){
            double[] ret=new double[a.length];
            for(int i=0;i<ret.length;i++){
                ret[i]=a[i]/b[i];
            }
            return ret;
        }else{
            throw new RuntimeException("The data shape is not correct");
        }
    }

    /**
     * 两个数组的点积，即元素对应相乘。如果两组数据都是一维数组则相当于内积
     * @param a
     * @param b
     * @return
     */
    public static double dot(double[] a,double[] b){
        return inner(a,b);
    }

    /**
     * 两个数组的点积
     * @param a
     * @param b
     * @return
     */
    public static double[][] dot(double[][] a,double[][] b){
        int rowA=a.length,colA=a[0].length,rowB=b.length,colB=b[0].length;
        if(colA==rowB){
            double[][] ret=new double[rowA][colB];
            for(int x=0;x<rowA;x++){
                for(int y=0;y<colB;y++){
                    double count=0;
                    for(int c=0;c<colA;c++){
                        count+=a[x][c]*b[c][y];
                    }
                    ret[x][y]=count;
                }
            }
            return ret;
        }else{
            throw new RuntimeException("The data shape is not correct");
        }
    }

    /**
     * 向量内积
     * @param a
     * @param b
     * @return
     */
    public static double inner(double[] a,double[] b){
        double ret=0;
        if(a.length!=b.length){
            throw new RuntimeException("The data shape is not correct");
        }
        for(int i=0;i<a.length;i++){
            ret+=a[i]*b[i];
        }
        return ret;
    }

    public static double inner(MultiDim a,MultiDim b){
        double ret=0;
        if(a.getShape().equals(b.getShape())){
            ShapeIndex index=new ShapeIndex(a.getShape());
            do{
                ret+=(double)a.getVal(index)*(double)b.getVal(index);
            }while(index.next());
        }
        return ret;
    }

    /**
     * 向量内积
     * @param a
     * @param b
     * @return
     */
    public static double[] inner(double[][] a,double[] b){
        double[] ret=new double[a.length];
        for(int i=0;i<a.length;i++){
            ret[i]=inner(a[i],b);
        }
        return ret;
    }

    /**
     * 向量内积
     * @param a
     * @param b
     * @param rowToCol
     * @return
     */
    public static double[] inner(double[][] a,double[] b,boolean rowToCol){
        if(rowToCol){
            return inner(rowToCol(a),b);
        }else{
            return inner(a,b);
        }
    }

    /**
     * 行转列
     * @param vals
     * @return
     */
    private static double[][] rowToCol(double[][] vals){
        double[][] ret=new double[vals[0].length][vals.length];
        for(int i=0;i<vals.length;i++){
            for(int j=0;j<vals[i].length;j++){
                ret[j][i]=vals[i][j];
            }
        }
        return ret;
    }
}
