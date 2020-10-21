package test.mnist;


import com.firefly.derivative.activation.NoneActivation;
import com.firefly.layers.core.Model;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.init.params.InitParamsRandomGaussian;
import com.firefly.layers.layers.Dense;
import com.firefly.layers.layers.Softmax;
import com.firefly.layers.listeners.FitControl;
import com.firefly.layers.listeners.LossCallBackListener;
import com.firefly.layers.loss.Cel;
import com.firefly.layers.models.Sequential;
import com.firefly.utils.ModelUtil;
import test.mnist.data.MnistRead;

public class MnistFit {
    private static String modelFile="src/test/resources/mnist_model/mnist_model.ser";

    public static void main(String[] args) {
        double[][] train_images = MnistRead.getImages(MnistRead.TRAIN_IMAGES_FILE);
        double[][] train_labels = one_hot(10,MnistRead.getLabels(MnistRead.TRAIN_LABELS_FILE));

        double[][] test_images = MnistRead.getImages(MnistRead.TEST_IMAGES_FILE);
        double[][] testlabels = one_hot(10,MnistRead.getLabels(MnistRead.TEST_LABELS_FILE));

        MultiDim[] train_x=arr2MultDim(train_images);
        MultiDim[] train_y=arr2MultDim(train_labels);
        MultiDim[] test_x=arr2MultDim(test_images);
        MultiDim[] test_y=arr2MultDim(testlabels);

        try{
//            Model model=new Sequential(0.00001);
//            model.add(new Dense(train_images[0].length,10, NoneActivation.class,new InitParamsRandomGaussian()));
//            model.add(new Softmax());
//    //        model.add(new Dropout(0.8f));
//    //        model.add(new Dense(10, LRelu.class));
//    //        model.add(new Dropout(0.9f));
//            //识差函数
//            model.setLossCls(Cel.class);
//            model.init();

            //导入并进行预测
            Model model=ModelUtil.importModel(modelFile);

            model.fit(train_x, train_y, 1000, 10,
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
                        public boolean onIsStop(int process,int epoch,double loss,long takeUpTime) {
                            //累计执行时间
                            countTime+=takeUpTime;
                            processTime+=takeUpTime;

                            if(loss<=0.0001){
                                System.out.println("第"+process+"次训练，满足条件自动退出训练！");
                                return true;
                            }else{
//                            if(process%100==99){
                                if(true){
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

            //导出到文件
            ModelUtil.exportModel(model,modelFile);
            //导入并进行预测
            Model newModel=ModelUtil.importModel(modelFile);

            showParams(newModel,test_x,test_y);
        }catch (Exception e){
            e.printStackTrace();
        }

        //将参数导入到新的模型里
//        importParams(model,x,y);
    }

    private static double[] one_hot(int len,int val){
        double[] ret=new double[len];
        ret[val]=1;
        return ret;
    }

    private static double[][] one_hot(int len,double[] vals){
        double[][] ret=new double[vals.length][];
        for(int i=0;i<vals.length;i++){
            ret[i]=one_hot(len,(int)vals[i]);
        }
        return ret;
    }

    /**
     * 获取最大值的下标
     * @param vals
     * @return
     */
    private static int maxIndex(double[] vals){
        int ret=0;
        double max=vals[0];
        for(int i=0;i<vals.length;i++){
            if(vals[i]>max){
                ret=i;
                max=vals[i];
            }
        }
        return ret;
    }

    private static MultiDim[] arr2MultDim(double[][] datas){
        MultiDim[] ret=new MultiDim[datas.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=new MultiDim(Double.TYPE,new Shape(new int[]{datas[i].length}),datas[i]);
        }
        return ret;
    }

    private static MultiDim[] arr2MultDim(double[] datas){
        MultiDim[] ret=new MultiDim[datas.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=new MultiDim(Double.TYPE,new Shape(new int[]{1}),new double[]{datas[i]});
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

        int errorNum=0;
        for(int i=0;i<x.length;i++){
            MultiDim py=model.predict(x[i]);
            int pi=maxIndex((double[])py.getData());
            int yi=maxIndex((double[])y[i].getData());
            if(pi==yi){
                System.out.println(yi+"     "+pi+"     ");
            }else{
                errorNum++;
                System.out.println(yi+"     "+pi+"     "+"      error");
            }
        }
        double rate=((double)(x.length-errorNum))/x.length;
        System.out.println(String.format("准确率：%.4f",rate));
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
