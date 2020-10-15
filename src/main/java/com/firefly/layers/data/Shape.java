package com.firefly.layers.data;

/**
 * 形状
 */
public class Shape implements java.io.Serializable{
    private int[] dims;//每个维度的数据数量
    private int[] dimsNum;//每个维度转为1维的数量

    /**
     * 判断是多少维
     * @return
     */
    public int howManyDim(){
        return dims.length;
    }

    public int[] getDims() {
        return dims;
    }
    public int getDim(int index){
        return dims[index];
    }

    public void setDims(int[] dims) {
        this.dims = dims;

        this.dimsNum=new int[dims.length];

        int num=dims[dims.length-1];
        this.dimsNum[dims.length-1]=num;
        for(int i=dims.length-2;i>=0;i--){
            num*=dims[i];
            this.dimsNum[i]=num;
        }
    }

    public Shape(){

    }

    public Shape(int[] dims) {
        setDims(dims);
    }

    public int getNums(){
        return getNums(0);
    }

    /**
     * 由1维下标获取多维下标
     * @param index
     * @return
     */
    public int[] getOneDim2MultDimIndex(int index){
        int[] indexs=new int[dimsNum.length];//1维下标转多维的数组
        int val=index;

        for(int i=0;i<dimsNum.length-1;i++){
            indexs[i]=val/dimsNum[i+1];//计算下标
            val=val%dimsNum[i+1];//求剩余多少下标
        }
        indexs[indexs.length-1]=val;

        return indexs;
    }

    /**
     * 获取第几维度的数组长度，比如3维(5,4,3)，获取第1维则返回3，如果获取第2维则返回4*3，如果获取第3维则返回5*4*3
     * @param dim
     * @return
     */
    public int getNums(int dim){
        return this.dimsNum[dim];
    }

    /**
     * 两个形状合并
     * @param a
     * @param b
     * @return
     */
    public static Shape merge(Shape a,Shape b){
        int[] dims=new int[a.getDims().length+b.getDims().length];
        System.arraycopy(a.getDims(),0,dims,0,a.getDims().length);
        System.arraycopy(b.getDims(),0,dims,a.getDims().length,b.getDims().length);
        return new Shape(dims);
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj){
            return true;
        }else{
            //任何对象不等于null，比较是否为同一类型
            if (!(obj instanceof Shape)) return false;

            Shape b=(Shape)obj;

            if(dims.length==b.getDims().length){
                for(int i=0;i<dims.length;i++){
                    if(dims[i]!=b.getDims()[i]){
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
