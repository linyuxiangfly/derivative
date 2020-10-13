package test.mask;

import com.firefly.derivative.activation.Relu;
import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Model;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.data.ThreeDimShape;
import com.firefly.layers.enums.Padding;
import com.firefly.layers.enums.PollingType;
import com.firefly.layers.init.params.InitParamsRandomGaussian;
import com.firefly.layers.layers.Conv;
import com.firefly.layers.layers.Dense;
import com.firefly.layers.layers.Pooling;
import com.firefly.layers.listeners.FitControl;
import com.firefly.layers.listeners.LossCallBackListener;
import com.firefly.layers.loss.Mse;
import com.firefly.layers.models.Sequential;
import test.mask.data.LoadData;

import java.io.*;
import java.util.List;

public class MaskConvPredict {

    private static String modelFile="src/main/resources/model/mask-conv.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        double[][][] xy=loadXY("src/main/resources/datas/train/mask.txt","src/main/resources/datas/train/nomask.txt");
        MultiDim[] x=arr2Image(xy[0],10,10,3);
        MultiDim[] y=arr2MultDim(xy[1]);

        double[][][] xyTest=loadXY("src/main/resources/datas/test/mask.txt","src/main/resources/datas/test/nomask.txt");
        MultiDim[] xTest=arr2Image(xyTest[0],10,10,3);
        MultiDim[] yTest=arr2MultDim(xyTest[1]);

        try{
            //导入并进行预测
            Model newModel=importModel(modelFile);

            showPredict(newModel,xTest,yTest);

            showParams(newModel);
        }catch (Exception e){
            e.printStackTrace();
        }

        //将参数导入到新的模型里
//        importParams(model,x,y);
    }

    private static MultiDim[] arr2MultDim(double[][] datas){
        MultiDim[] ret=new MultiDim[datas.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=new MultiDim(Double.TYPE,new Shape(new int[]{datas[i].length}),datas[i]);
        }
        return ret;
    }

    private static MultiDim[] arr2Image(double[][] datas,int height,int width,int rgb){
        MultiDim[] ret=new MultiDim[datas.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=new MultiDim(Double.TYPE,new ThreeDimShape(height,width,rgb),one2ThreeDim(datas[i],height,width,rgb));
        }
        return ret;
    }

    /**
     * 1维数据转3维
     * @param data
     * @return
     */
    private static double[][][] one2ThreeDim(double[] data,int height,int width,int rgb){
        double[][][] ret=new double[height][width][rgb];
        for(int x=0;x<height;x++){
            for(int y=0;y<width;y++){
                for(int z=0;z<rgb;z++){
                    ret[x][y][z]=data[x*width*rgb+y*rgb+z];
                }
            }
        }
        return ret;
    }

    private static double[][][] loadXY(String maskFile,String nomaskFile) throws IOException {
        List<double[]> maskList= LoadData.load(new File(maskFile));
        List<double[]> nomaskList= LoadData.load(new File(nomaskFile));

        double[][] maskY=createArray(maskList.size(),new double[]{100.0,0.0});
        double[][] nomaskY=createArray(nomaskList.size(),new double[]{0.0,100.0});

        double[][] x=mergeArrays(maskList,nomaskList);
        double[][] y=mergeArrays(maskY,nomaskY);
        return new double[][][]{
                x,y
        };
    }


    private static double[][] createArray(int rows,double[] rowVal){
        double[][] ret=new double[rows][];
        for(int i=0;i<ret.length;i++){
            ret[i]=rowVal.clone();
        }
        return ret;
    }

    private static double[][] mergeArrays(List<double[]> a,List<double[]> b){
        double[][] ret=new double[a.size()+b.size()][];
        for(int i=0;i<a.size();i++){
            ret[i]=a.get(i);
        }
        for(int i=0;i<b.size();i++){
            ret[a.size()+i]=b.get(i);
        }
        return ret;
    }

    private static double[][] mergeArrays(double[][] a,double[][] b){
        double[][] ret=new double[a.length+b.length][];
        for(int i=0;i<a.length;i++){
            ret[i]=a[i];
        }
        for(int i=0;i<b.length;i++){
            ret[a.length+i]=b[i];
        }
        return ret;
    }

    /**
     * 将mode导出文件
     * @param model
     */
    private static void exportModel(Model model,String file) throws IOException {
        FileOutputStream fileOut =
                new FileOutputStream(new File(file));
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(model);
        out.close();
        fileOut.close();
        System.out.printf("Serialized data is saved in /tmp/employee.ser\n");
    }

    private static Model importModel(String file) throws IOException, ClassNotFoundException {
        //新建一个模型，将之前模型的参数导入再进行拟合
        Model newModel=null;

        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        newModel = (Model) in.readObject();
        in.close();
        fileIn.close();

        return newModel;
    }

    private static void showPredict(Model model,MultiDim[] x,MultiDim[] y){
        for(int i=0;i<x.length;i++){
            MultiDim py=model.predict(x[i]);

            ShapeIndex j=new ShapeIndex(py.getShape());
            do{
                System.out.print(String.format("%.10f   ", (double)py.getVal(j)));
            }while(j.next());

            j=new ShapeIndex(y[i].getShape());
            do{
                System.out.print(String.format("%.10f   ", (double)y[i].getVal(j)));
            }while(j.next());

            j=new ShapeIndex(py.getShape());
            double[] pyVal=(double[])py.getData();
            double[] yVal=(double[])y[i].getData();
            boolean pb=pyVal[0]>pyVal[1];
            boolean yb=yVal[0]>yVal[1];
            if(pb!=yb){
                System.out.print("      error!");
            }

            System.out.println();
        }
    }

    private static void showParams(Model model){
        int i=0;
        for(Layer layer:model.getLayers()){
            if(layer.getW()!=null && layer.getB()!=null){
                switch(layer.getW().getShape().getDims().length){
                    case 1:
                        break;
                    case 2:
                        printArray("第"+(i+1)+"层的W",(double[][])layer.getW().getData());
                        break;
                    case 3:
                        printArray("第"+(i+1)+"层的W",(double[][][])layer.getW().getData());
                        break;
                    case 4:
                        printArray("第"+(i+1)+"层的W",(double[][][][])layer.getW().getData());
                        break;
                }

                switch(layer.getB().getShape().getDims().length){
                    case 1:
                        printArray("第"+(i+1)+"层的B",(double[])layer.getB().getData());
                        break;
                    case 2:
                        printArray("第"+(i+1)+"层的B",(double[][])layer.getB().getData());
                        break;
                    case 3:
                        break;
                }
            }
            i++;
        }
    }

    private static void printArray(String title,double[][][] vals){
        if(!title.isEmpty()){
            System.out.println(title);
        }

        System.out.println("{");
        for(int j=0;j<vals.length;j++){
            printArray("",vals[j]);
        }
        System.out.println("}");
    }

    private static void printArray(String title,double[][][][] vals){
        if(!title.isEmpty()){
            System.out.println(title);
        }

        System.out.println("{");
        for(int j=0;j<vals.length;j++){
            printArray("",vals[j]);
        }
        System.out.println("}");
    }

    private static void printArray(String title,double[][] vals){
        if(!title.isEmpty()){
            System.out.println(title);
        }

        System.out.println("{");
        for(int j=0;j<vals.length;j++){
            printArray("",vals[j]);
        }
        System.out.println("}");
    }

    private static void printArray(String title,double[] vals){
        if(!title.isEmpty()){
            System.out.println(title);
        }

        System.out.print("{");
        for(int i=0;i<vals.length;i++){
            if(i==0){
                System.out.print(vals[i]);
            }else{
                System.out.print(","+vals[i]);
            }
        }
        System.out.println("}");
    }
}