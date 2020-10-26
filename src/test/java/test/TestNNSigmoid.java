package test;

import com.firefly.derivative.activation.Sigmoid;
import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Model;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.init.params.InitParamsRandomGaussian;
import com.firefly.layers.layers.Dense;
import com.firefly.layers.layers.Dropout;
import com.firefly.layers.listeners.FitControl;
import com.firefly.layers.listeners.LossCallBackListener;
import com.firefly.layers.loss.Mse;
import com.firefly.layers.models.Sequential;
import com.firefly.layers.optimizer.Adam;
import com.firefly.layers.optimizer.Sgd;
import com.firefly.utils.ModelUtil;

import java.io.*;

public class TestNNSigmoid {

    private static String modelFile="d:/test.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MultiDim[] x=arr2MultDim(new double[][]{
                {1,1,1,1,0,1,1,0,1,1,1,1},
                {0,1,1,1,0,1,1,0,1,1,1,1},
                {1,1,0,1,0,1,1,0,1,1,1,1},
                {1,1,1,1,0,1,1,0,1,1,1,0},
                {1,1,1,1,0,1,1,0,1,0,1,1},
                {0,0,0,1,1,1,1,0,1,1,1,1},
                {0,0,0,0,1,1,1,0,1,1,1,1},
                {0,0,0,1,1,0,1,0,1,1,1,1},
                {0,0,0,1,1,1,1,0,1,1,1,0},
                {0,0,0,1,1,1,1,0,1,0,1,1},
                {1,1,1,1,0,1,1,1,1,0,0,0},
                {0,1,1,1,0,1,1,1,1,0,0,0},
                {1,1,0,1,0,1,1,1,1,0,0,0},
                {1,1,1,1,0,1,1,1,0,0,0,0},
                {1,1,1,1,0,1,0,1,1,0,0,0},
                {1,0,1,1,0,1,1,0,1,1,1,1},
                {1,1,1,1,0,0,1,0,1,1,1,1},
                {1,1,1,1,0,1,1,0,0,1,1,1},
                {1,1,1,1,0,1,1,0,1,1,0,1},
                {1,1,1,1,0,1,0,0,1,1,1,1},
                {1,1,1,0,0,1,1,0,1,1,1,1},
                {0,0,1,1,0,1,1,0,1,1,1,1},
                {0,1,1,1,0,0,1,0,1,1,1,1},
                {0,1,1,1,0,1,1,0,0,1,1,1},
                {0,1,1,1,0,1,1,0,1,1,0,1},
                {0,1,1,1,0,1,0,0,1,1,1,1},
                {0,1,1,0,0,1,1,0,1,1,1,1},
                {1,1,0,1,0,0,1,0,1,1,1,1},
                {1,1,0,1,0,1,1,0,0,1,1,1},
                {1,1,0,1,0,1,1,0,1,1,0,1},
                {1,1,0,1,0,1,0,0,1,1,1,1},
                {1,1,0,0,0,1,1,0,1,1,1,1},
                {0,1,0,0,1,0,0,1,0,0,1,0},
                {1,1,0,0,1,0,0,1,0,0,1,0},
                {0,1,0,0,1,0,0,1,0,0,1,0},
                {0,1,0,0,1,0,0,1,0,1,1,0},
                {0,1,0,0,1,0,0,1,0,0,1,1},
                {1,1,0,0,1,0,0,1,0,1,1,0},
                {1,1,0,0,1,0,0,1,0,0,1,1},
                {1,1,0,0,1,0,0,1,0,1,1,1},
                {0,1,0,0,1,1,0,1,0,0,1,0},
                {0,1,0,0,1,0,0,1,1,0,1,0},
                {1,1,0,0,1,1,0,1,0,0,1,0},
                {1,1,0,0,1,0,0,1,1,0,1,0},
                {0,1,0,0,1,1,0,1,0,1,1,0},
                {0,1,0,0,1,0,0,1,1,1,1,0},
                {0,1,0,0,1,0,0,1,0,1,1,1},
                {1,1,0,0,1,1,0,1,1,0,1,1},
                {1,1,0,0,1,0,0,1,0,0,1,0},
                {0,1,1,0,1,1,0,1,1,0,1,1},
                {1,1,0,1,1,0,0,1,0,0,1,0},
                {1,1,0,0,1,0,1,1,0,0,1,0},
                {1,1,0,1,1,0,1,1,0,1,1,0},
                {1,1,0,0,1,0,0,0,0,0,1,0},
                {0,1,0,0,1,0,0,1,0,1,0,0},
                {1,0,0,0,1,0,0,1,0,0,1,0},
                {1,0,0,0,1,0,0,1,0,0,0,1},
                {0,1,0,0,0,0,0,1,0,1,1,0},
                {0,1,0,0,1,0,0,0,0,1,1,0},
                {0,0,0,0,1,0,0,1,0,1,1,0},
                {0,0,0,0,1,0,0,1,0,0,1,0},
                {0,1,0,0,1,0,0,1,0,0,0,0},
                {0,1,0,0,0,1,0,0,1,0,1,0},
                {0,1,0,1,0,0,1,0,0,0,1,0}
        });

        MultiDim[] y=arr2MultDim(new double[][]{
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {1,0},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1},
                {0,1}
        });

        Model model=new Sequential();
        model.add(new Dense(12,6,new Adam(0.1,0.9,0.999), ()-> new Sigmoid(),new InitParamsRandomGaussian()));
        model.add(new Dropout(0.8f));
        model.add(new Dense(2, new Adam(0.1,0.9,0.999),()->new Sigmoid()));
//        model.add(new Dropout(0.9f));
        //识差函数
        model.setLossCls(Mse.class);
        model.init();

        model.fit(x, y, 100, 10,
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

                        if(loss<=0){
                            System.out.println("第"+process+"次训练，满足条件自动退出训练！");
                            return true;
                        }else{
                            if(process%100==99){
                                double c=processTime/1000.0;
                                double processRate=(process+1.0)/epoch;
                                double sum=countTime/processRate/60.0/1000.0;//按分钟计算

                                System.out.println("第"+process+"次训练！ "+"    takeUpTime:"+String.format("%.2f 秒", c)+"    TotalTime:"+String.format("%.2f 分钟", sum)+"    loss:"+String.format("%.10f", loss));

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

            showParams(newModel,x,y);
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

    private static void showParams(Model model,MultiDim[] x,MultiDim[] y){
        //新建一个模型，将之前模型的参数导入再进行拟合
//        Model newModel=new Sequential(0.1);
//        newModel.add(new Dense(12,3, Sigmoid.class,new InitParamsRandomGaussian()));
//        newModel.add(new Dense(2, Sigmoid.class));
//        //识差函数
//        newModel.setLossCls(Mse.class);
//        newModel.init();
//
//        for(int i=0;i<model.getLayers().size();i++){
//            Layer nl=newModel.getLayers().get(i);
//            Layer ol=model.getLayers().get(i);
//            nl.setW(ol.getW());
//            nl.setB(ol.getB());
//        }

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

            System.out.println();
        }

        int i=0;
        for(Layer layer:model.getLayers()){
            if(layer.getW()!=null && layer.getB()!=null){
                printArray("第"+(i+1)+"层的W",(double[][])layer.getW().getData());
                printArray("第"+(i+1)+"层的B",(double[])layer.getB().getData());
            }
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
