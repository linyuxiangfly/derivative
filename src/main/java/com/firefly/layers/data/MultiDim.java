package com.firefly.layers.data;

import java.lang.reflect.Array;

/**
 * 多维数据
 */
public class MultiDim implements java.io.Serializable{
    private Class<?> type;
    private Shape shape;
    private Object data;

    public MultiDim(Shape shape){
        this(Double.TYPE,shape);
    }

    public MultiDim(Class<?> type,Shape shape){
        this(type,shape, Array.newInstance(type,shape.getDims()));
    }

    public MultiDim(Class<?> type,Shape shape,Object data){
        this.type=type;
        this.shape=shape;
        this.data=data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData(){
        return data;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * 由1维下标获取多维下标
     * @param index
     * @return
     */
    public Object getOneDim2MultDimIndexVal(int index){
        int[] dimIndex=this.getShape().getOneDim2MultDimIndex(index);

        return getVal(data,dimIndex);
    }

    /**
     * 获取下标值
     * @param shapeIndex
     * @return
     */
    public Object getVal(ShapeIndex shapeIndex){
        return getVal(shapeIndex.getDimIndex());
    }

    public Object getVal(ShapeIndex[] indices){
        return getVal(data,indices);
    }

    private Object getVal(Object data,ShapeIndex[] indices){
        Object ret=data;
        for(int i=0;i<indices.length;i++){
            ret=getVal(ret,indices[i]);
        }
        return ret;
    }

    private Object getVal(Object data,ShapeIndex index){
        return getVal(data,index.getDimIndex());
    }

    private Object getVal(Object data,int[] dimIndex){
        Object obj=data;
        for(int index: dimIndex){
            obj = Array.get(obj, index);
        }
        return obj;
    }

    /**
     * 获取下标值
     * @param dimIndex
     * @return
     */
    public Object getVal(int[] dimIndex){
        Object obj=data;
        for(int index: dimIndex){
            obj = Array.get(obj, index);
        }
        return obj;
    }

    /**
     * 设置下标值
     * @param shapeIndex
     * @param val
     */
    public void setVal(ShapeIndex shapeIndex, Object val){
        setVal(shapeIndex.getDimIndex(),val);
    }

    private void setVal(Object data,int[] dimIndex, Object val){
        Object obj=data;

        //循环到倒数第2个维
        for(int i=0;i<dimIndex.length-1;i++){
            obj = Array.get(obj, dimIndex[i]);
        }
        Array.set(obj,dimIndex[dimIndex.length-1],val);
    }

    public void setVal(ShapeIndex[] indices, Object val){
        Object obj=data;

        //循环到倒数第2个维
        for(int i=0;i<indices.length-1;i++){
            obj=getVal(obj,indices[i]);
        }

        setVal(obj,indices[indices.length-1].getDimIndex(),val);
    }

    /**
     * 设置下标值
     * @param dimIndex
     * @param val
     */
    public void setVal(int[] dimIndex, Object val){
        Object obj=data;

        //循环到倒数第2个维
        for(int i=0;i<dimIndex.length-1;i++){
            obj = Array.get(obj, dimIndex[i]);
        }
        Array.set(obj,dimIndex[dimIndex.length-1],val);
    }

    /**
     * 设置所有值
     * @param val
     */
    public void setAll(Object val){
        ShapeIndex index=new ShapeIndex();
        do{
            setVal(index,val);
        }while (index.next());
    }
}
