package test;

import com.firefly.derivative.activation.Sigmoid;
import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Model;
import com.firefly.layers.init.params.InitParamsRandomGaussian;
import com.firefly.layers.layers.Dense;
import com.firefly.layers.listeners.FitControl;
import com.firefly.layers.listeners.LossCallBackListener;
import com.firefly.layers.loss.Mse;
import com.firefly.layers.models.Sequential;

import java.io.*;

public class TestNNSigmoid {

    private static String modelFile="f:/test.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        double[][] x=new double[][]{
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
        };

        double[][] y=new double[][]{
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
        };

        Model model=new Sequential(0.1);
        model.add(new Dense(12,3, Sigmoid.class,new InitParamsRandomGaussian()));
        model.add(new Dense(2, Sigmoid.class));
        //识差函数
        model.setLossCls(Mse.class);
        model.init();

        model.fit(x, y, 10000, 10,
                new LossCallBackListener() {
                    @Override
                    public void onLoss(double val) {
                        System.out.println(String.format("%.10f", val));
                    }
                },
                new FitControl() {
                    @Override
                    public boolean onIsStop(int epoch, double loss) {
                        if(loss<=0.001){
                            System.out.println("第"+epoch+"次训练，满足条件自动退出训练！");
                            return true;
                        }
                        return false;
                    }
                }
        );

        try{
            //导出到文件
            exportModel(model,modelFile);
            //导入并进行预测
            Model newModel=importModel(modelFile);

            showParams(newModel,x,y);
        }catch (Exception e){
            e.printStackTrace();
        }

        //将参数导入到新的模型里
//        importParams(model,x,y);
    }

    /**
     * 将mode导出文件
     * @param model
     */
    private static void exportModel(Model model,String file) throws IOException {
        FileOutputStream fileOut =
                new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(model);
        out.close();
        fileOut.close();
        System.out.printf("Serialized data is saved in /tmp/employee.ser");
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

    private static void showParams(Model model,double[][] x,double[][] y){
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
            double[] py=model.predict(x[i]);
            for(int j=0;j<py.length;j++){
                System.out.print(String.format("%.10f   ", py[j]));
            }
            for(int j=0;j<y[i].length;j++){
                System.out.print(String.format("%.10f   ", y[i][j]));
            }
            System.out.println();
        }

        int i=0;
        for(Layer layer:model.getLayers()){
            printArray("第"+(i+1)+"层的W",layer.getW());
            printArray("第"+(i+1)+"层的B",layer.getB());
            i++;
        }
    }

    private static void printArray(String title,double[][] vals){
        System.out.println(title);
        System.out.println("{");
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
