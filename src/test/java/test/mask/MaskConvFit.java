package test.mask;

import com.firefly.derivative.activation.LRelu;
import com.firefly.derivative.activation.NoneActivation;
import com.firefly.derivative.activation.Sigmoid;
import com.firefly.derivative.core.Function;
import com.firefly.derivative.operation.Const;
import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Model;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.data.ThreeDimShape;
import com.firefly.layers.enums.Padding;
import com.firefly.layers.enums.PollingType;
import com.firefly.layers.init.params.InitParamsRandomGaussian;
import com.firefly.layers.layers.*;
import com.firefly.layers.listeners.FitControl;
import com.firefly.layers.listeners.LossCallBackListener;
import com.firefly.layers.loss.Cel;
import com.firefly.layers.loss.Mse;
import com.firefly.layers.models.Sequential;
import com.firefly.layers.optimizer.Adam;
import com.firefly.layers.optimizer.Sgd;
import com.firefly.utils.ModelUtil;
import test.mask.data.LoadData;

import java.io.*;
import java.util.List;

public class MaskConvFit {

    private static String modelFile="src/test/resources/model/mask-conv.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        double[][][] xy=loadXY("src/test/resources/datas/train/mask.txt","src/test/resources/datas/train/nomask.txt");
        MultiDim[] x=arr2Image(xy[0],10,10,3);
        MultiDim[] y=arr2MultDim(xy[1]);

        double[][][] xyTest=loadXY("src/test/resources/datas/test/mask.txt","src/test/resources/datas/test/nomask.txt");
        MultiDim[] xTest=arr2Image(xyTest[0],10,10,3);
        MultiDim[] yTest=arr2MultDim(xyTest[1]);

        Model model=new Sequential();
        model.add(new Conv(
                (ThreeDimShape) x[0].getShape(),
                32,
                3,
                1,
                Padding.same,
                new Adam(0.001,0.9,0.999),
                () -> {
                    LRelu a=new LRelu();
                    a.setMinVal(0.01);
                    return a;
                },
                new InitParamsRandomGaussian()));
        model.add(new Pooling(PollingType.max,2));
//        model.add(new Zoom(-10f,10f,0f,1f));
//        model.add(new Conv(16,3,1, Padding.same, LRelu.class,new Function[]{new Const(0.01)},new InitParamsRandomGaussian()));
//        model.add(new Pooling(PollingType.max,2,2,true));
        model.add(new Flatten());
//        model.add(new Dropout(0.5f));
//        model.add(new Dense(10, LRelu.class));
        model.add(new Dense(
                2,
                new Adam(0.001,0.9,0.999),
                () -> {
                    Sigmoid a=new Sigmoid();
                    return a;
                }
        ));
//        model.add(new Softmax());
        //识差函数
        model.setLossCls(Mse.class);
        model.init();

        model.fit(x, y, 1000, 4,
                new LossCallBackListener() {
                    @Override
                    public void onLoss(double val) {
//                        System.out.println(String.format("%.10f", val));
                    }
                },
                new FitControl() {
                    private long countTime=0;
                    private long processTime=0;

                    @Override
                    public void onProcess(int process, int epoch, double currentProgress, double loss,long takeUpTime) {

                    }

                    @Override
                    public boolean onIsStop(int process,int epoch,double loss,long takeUpTime) {
                        //累计执行时间
                        countTime+=takeUpTime;
                        processTime+=takeUpTime;

                        if(loss<=0.0001){
                            System.out.println("第"+process+"次训练，满足条件自动退出训练！");
                            return true;
                        }else{
                            if(process%99==0){
                                double c=processTime/1000.0;
                                double processRate=(process+1.0)/epoch;
                                double left=(countTime/processRate-countTime)/60.0/1000.0;//按分钟计算

                                System.out.println(process+"/"+epoch+"    当次执行时间:"+String.format("%.2f 秒", c)+"    剩下时间:"+String.format("%.2f 分钟", left)+"    误差:"+String.format("%.10f", loss));

                                processTime=0;
                            }
                        }
                        return false;
                    }
                }
        );

        try{
            //导出到文件
            ModelUtil.exportModel(model,modelFile);
            //导入并进行预测
            Model newModel=ModelUtil.importModel(modelFile);

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

        double[][] maskY=createArray(maskList.size(),new double[]{1.0,0.0});
        double[][] nomaskY=createArray(nomaskList.size(),new double[]{0.0,1.0});

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
                double diff=Math.abs((double)py.getVal(j)-(double)y[i].getVal(j));
                if(diff>=0.1){
                    System.out.print(String.format("diff:%.10f   ", diff));
                }
            }while(j.next());

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
