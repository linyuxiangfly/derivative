package com.firefly.layers.data;

/**
 * 形状
 */
public class Shape implements java.io.Serializable{
    private int[] dims;//每个维度的数据数量

    public int[] getDims() {
        return dims;
    }
    public int getDim(int index){
        return dims[index];
    }

    public void setDims(int[] dims) {
        this.dims = dims;
    }

    public Shape(){

    }

    public Shape(int[] dims) {
        this.dims = dims;
    }

    public int getNums(){
        int ret=0;
        for(int num:dims){
            ret+=num;
        }
        return ret;
    }

    /**
     * 获取第几维度的数组长度，比如3维(5,4,3)，获取第1维则返回3，如果获取第2维则返回4*3，如果获取第3维则返回5*4*3
     * @param dim
     * @return
     */
    public int getNums(int dim){
        int ret=dims[0];
        for(int i=1;i<dim;i++){
            ret*=dims[i];
        }
        return ret;
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
