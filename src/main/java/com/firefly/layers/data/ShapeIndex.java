package com.firefly.layers.data;

/**
 * 形状，对应的位置
 */
public class ShapeIndex implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Shape shape;
    private int[] dimIndex;//每个维度的下标

    public int[] getDimIndex() {
        return dimIndex;
    }

    public void setDimIndex(int[] dimIndex) {
        this.dimIndex = dimIndex;
    }

    public ShapeIndex(){

    }

    public ShapeIndex(int dimNum){
        this(new int[dimNum]);
    }

    public ShapeIndex(int[] dimIndex) {
        this(null,dimIndex);
    }

    public ShapeIndex(Shape shape) {
        this(shape,new int[shape.getDims().length]);
    }

    public ShapeIndex(Shape shape,int[] dimIndex){
        this.shape=shape;
        this.dimIndex=dimIndex;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * 设置维度的下标值
     * @param index
     * @param val
     */
    public void setDimIndexVal(int index,int val){
        dimIndex[index]=val;
    }

    /**
     * 设置维度的下标值
     * @param index
     * @return
     */
    public int getDimIndexVal(int index){
        return dimIndex[index];
    }

    /**
     * 由1维下标获取多维下标
     * @param index
     * @return
     */
    public int[] getOneDim2MultDimIndex(int index){
        int[] indexs=new int[this.getShape().getDims().length];//1维下标转多维的数组
        int val=index;

        for(int i=0;i<this.getShape().getDims().length-1;i++){
            indexs[i]=val/this.getShape().getNums(i+1);//计算下标
            val=val%this.getShape().getNums(i+1);//求剩余多少下标
        }
        indexs[indexs.length-1]=val;

        return indexs;
    }

    /**
     * 获取多维转1维的下标
     * @return
     */
    public int getMultDim2OneDimIndex(){
        int ret=0;
        for(int i=0;i<dimIndex.length-1;i++){
            ret+=this.getShape().getNums(i+1)*dimIndex[i];
        }
        //加上最后一维
        ret+=dimIndex[dimIndex.length-1];
        return ret;
    }

    /**
     * 将下标置0
     */
    public void zero(){
        for(int i=0;i<dimIndex.length;i++){
            dimIndex[i]=0;
        }
    }

    public void last(){
        int[] dims=shape.getDims();
        for(int i=0;i<dimIndex.length;i++){
            dimIndex[i]=dims[i]-1;
        }
    }

    /**
     * 下一个下标
     */
    public boolean next(){
        //第最后一维开始
        for(int i=dimIndex.length-1;i>=0;i--){
            //如果没有超出当前维度的下标
            if(dimIndex[i]+1<shape.getDim(i)){
                dimIndex[i]++;
                return true;
            }else{
                //当前维度从头开始
                dimIndex[i]=0;
            }
        }
        last();
        return false;
    }

    /**
     * 上一个下标
     */
    public boolean back(){
        //第最后一维开始
        for(int i=dimIndex.length-1;i>=0;i--){
            //如果没有超出当前维度的下标
            if(dimIndex[i]==0){
                dimIndex[i]=shape.getDim(i)-1;
            }else{
                dimIndex[i]--;
                return true;
            }
        }
        zero();
        return false;
    }

    /**
     * 是否有下一个
     * @return
     */
    public boolean hasNext(){
        //第最后一维开始
        for(int i=0;i<dimIndex.length;i++){
            //如果没有超出当前维度的下标
            if(dimIndex[i]+1<shape.getDim(i)){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有上一个
     */
    public boolean hasBack(){
        //第最后一维开始
        for(int i=dimIndex.length-1;i>=0;i--){
            //如果没有超出当前维度的下标
            if(dimIndex[i]>0){
                return true;
            }
        }
        return false;
    }

    /**
     * 复制值
     * @param val
     */
    public void copy(ShapeIndex val){
        for(int i=0;i<val.getDimIndex().length;i++){
            this.dimIndex[i]=val.getDimIndexVal(i);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj){
            return true;
        }else{
            //任何对象不等于null，比较是否为同一类型
            if (!(obj instanceof ShapeIndex)) return false;

            ShapeIndex b=(ShapeIndex)obj;

            if(dimIndex.length==b.getDimIndex().length){
                for(int i=0;i<dimIndex.length;i++){
                    if(dimIndex[i]!=b.getDimIndex()[i]){
                        return false;
                    }
                }
                return true;
            }else{
                return false;
            }
        }
    }
}
