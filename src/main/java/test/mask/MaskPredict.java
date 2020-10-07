package test.mask;

import com.firefly.derivative.activation.Sigmoid;
import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Model;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.init.params.InitParamsRandomGaussian;
import com.firefly.layers.layers.Dense;
import com.firefly.layers.listeners.FitControl;
import com.firefly.layers.listeners.LossCallBackListener;
import com.firefly.layers.loss.Mse;
import com.firefly.layers.models.Sequential;
import test.mask.data.LoadData;

import java.io.*;
import java.util.List;

public class MaskPredict {

    private static String modelFile="src/main/resources/model/mask.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        double[][][] xy=loadXY("src/main/resources/datas/train/mask.txt","src/main/resources/datas/train/nomask.txt");
        MultiDim[] x=arr2MultDim(xy[0]);
        MultiDim[] y=arr2MultDim(xy[1]);

        double[][][] xyTest=loadXY("src/main/resources/datas/test/mask.txt","src/main/resources/datas/test/nomask.txt");
        MultiDim[] xTest=arr2MultDim(xyTest[0]);
        MultiDim[] yTest=arr2MultDim(xyTest[1]);

        try{
            //导入并进行预测
            Model newModel=importModel(modelFile);

            double error=0,error2=0;

            long start=System.currentTimeMillis();

            for(int i=0;i<1000;i++){
                error=1-calcPredictError(newModel,x,y);
//            System.out.println(String.format("diff:%.10f   ", error));
                error2=1-calcPredictError(newModel,xTest,yTest);
//            System.out.println(String.format("diff:%.10f   ", error));
            }

            long end=System.currentTimeMillis();
            System.out.println("time(ms):"+((end-start)/1000.0)+"   Accuracy:"+String.format("%.10f   ", error)+"   Accuracy2:"+String.format("%.10f   ", error2));
            System.out.println();

            showPredict(newModel,x,y);
            showPredict(newModel,xTest,yTest);

//            showParams(newModel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static MultiDim[] arr2MultDim(double[][] datas){
        MultiDim[] ret=new MultiDim[datas.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=new MultiDim(Double.TYPE,new Shape(new int[]{datas[i].length}),datas[i]);
        }
        return ret;
    }

    private static double[][][] loadXY(String maskFile,String nomaskFile) throws IOException {
        List<double[]> maskList= LoadData.load(new File(maskFile));
        List<double[]> nomaskList= LoadData.load(new File(nomaskFile));

        double[][] maskY=createArray(maskList.size(),new double[]{1.0});
        double[][] nomaskY=createArray(nomaskList.size(),new double[]{0.0});

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

    private static Model importModel(String file) throws IOException, ClassNotFoundException {
        //新建一个模型，将之前模型的参数导入再进行拟合
        Model newModel=null;

        FileInputStream fileIn = new FileInputStream(new File(file));
        ObjectInputStream in = new ObjectInputStream(fileIn);
        newModel = (Model) in.readObject();
        in.close();
        fileIn.close();

        return newModel;
    }

    private static double calcPredictError(Model model,MultiDim[] x,MultiDim[] y){
        double errorNum=0;
        for(int i=0;i<x.length;i++){
            MultiDim py=model.predict(x[i]);

            ShapeIndex j=new ShapeIndex(py.getShape());
            do{
//                System.out.print(String.format("%.10f   ", (double)py.getVal(j)*100));
                double diff=Math.abs((double)py.getVal(j)-(double) y[i].getVal(j));
                if(diff>=0.05){
                    errorNum++;
                    break;
                }
            }while(j.next());
        }
        return errorNum/x.length;
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
            do{
                System.out.print(String.format("%.10f   ", (double)py.getVal(j)*100));
                double diff=Math.abs((double)py.getVal(j)-(double) y[i].getVal(j));
                if(diff>=0.05){
                    System.out.print(String.format("diff:%.10f   ", diff));
                }
            }while(j.next());

            System.out.println();
        }
    }

    private static void showParams(Model model){
        int i=0;
        for(Layer layer:model.getLayers()){
            printArray("第"+(i+1)+"层的W",(double[][])layer.getW().getData());
            printArray("第"+(i+1)+"层的B",(double[])layer.getB().getData());
            i++;
        }
    }

    private static void printArray(String title,double[][] vals){
        System.out.println(title);
        System.out.println("\n{");
        for(int j=0;j<vals.length;j++){
            System.out.print("{");
            for(int i=0;i<vals[j].length;i++){
                if(i==0){
                    System.out.print(vals[j][i]);
                }else{
                    System.out.print(","+vals[j][i]);
                }
            }

            if(j<vals.length-1){
                System.out.println("},");
            }else{
                System.out.println("}");
            }
        }
        System.out.println("}");
        System.out.println();
    }

    private static void printArray(String title,double[] vals){
        System.out.println(title);
        System.out.print("{");
        for(int i=0;i<vals.length;i++){
            if(i==0){
                System.out.print(vals[i]);
            }else{
                System.out.print(","+vals[i]);
            }
        }
        System.out.println("}");
        System.out.println();
    }
}
