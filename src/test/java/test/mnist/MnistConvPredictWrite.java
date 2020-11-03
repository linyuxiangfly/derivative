package test.mnist;

import com.firefly.layers.core.Model;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ThreeDimShape;
import com.firefly.utils.ModelUtil;
import test.mnist.data.LocalRead;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MnistConvPredictWrite {
    private static String modelFile="src/test/resources/mnist_model/conv/";
    private static String writePath="src/test/resources/mnist_write/";

    public static void main(String[] args) {
        File[] files=getDirFiles(new File(writePath));
        double[][] test_images = LocalRead.getImages(files);
        double[][] testlabels = one_hot(10,LocalRead.getLabels(files));

        MultiDim[] test_x=arr2MultDimThreeDim(test_images,new ThreeDimShape(28,28,1));
        MultiDim[] test_y=arr2MultDim(testlabels);

        try{
            int num=getLastModelFileNum(new File(modelFile));
            //导入并进行预测
            String fileName=num+"";
            //导入并进行预测
            Model newModel=ModelUtil.importModel(modelFile+fileName);

            long start=System.currentTimeMillis();
//            showParams(newModel,train_x,train_y);
            showParams(newModel,test_x,test_y);

            long end=System.currentTimeMillis();
            System.out.println("time(s):"+((end-start)/1000.0));
            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }

        //将参数导入到新的模型里
//        importParams(model,x,y);
    }

    private static int getLastModelFileNum(File dir){
        int max=-1;
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            throw new RuntimeException("dir is not exists");
        }
        String[] files = dir.list();// 读取目录下的所有目录文件信息
        for (int i = 0; i < files.length; i++) {// 循环，添加文件名或回调自身
            File file = new File(dir, files[i]);
            if (file.isFile()) {// 如果文件
                String name=file.getName();
                Integer num=Integer.parseInt(name);
                if(num>max){
                    max=num;
                }
            }
        }
        return max;
    }

    private static File[] getDirFiles(File dir){
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            throw new RuntimeException("dir is not exists");
        }

        String[] files = dir.list();// 读取目录下的所有目录文件信息
        File[] retVal=new File[files.length];

        for (int i = 0; i < files.length; i++) {// 循环，添加文件名或回调自身
            File file = new File(dir, files[i]);
            if (file.isFile()) {// 如果文件
                retVal[i]=file;
            }
        }
        return retVal;
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

    private static MultiDim[] arr2MultDimThreeDim(double[][] datas, ThreeDimShape shape){
        MultiDim[] ret=new MultiDim[datas.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=arr2MultDimThreeDim(datas[i],shape);
        }
        return ret;
    }

    private static MultiDim arr2MultDimThreeDim(double[] data,ThreeDimShape shape){
        MultiDim ret=new MultiDim(Double.TYPE,shape);
        double[][][] retData=(double[][][])ret.getData();
        for(int x=0;x<shape.getX();x++){
            for(int y=0;y<shape.getY();y++){
                for(int z=0;z<shape.getZ();z++){
                    retData[x][y][z]=data[x*shape.getNums(1)+y*shape.getNums(2)+z];
                }
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
//                System.out.println(yi+"     "+pi+"     ");
            }else{
                errorNum++;
//                saveFile((double[][][])x[i].getData(),errorPath+yi+"-"+pi+".png");
                System.out.println(yi+"     "+pi+"     "+"      error");
            }
        }
        double rate=((double)(x.length-errorNum))/x.length;
        System.out.println(String.format("准确率：%.4f",rate));

//        int i=0;
//        for(Layer layer:model.getLayers()){
//            if(layer.getW()!=null && layer.getB()!=null){
//                printArray("第"+(i+1)+"层的W",(double[][])layer.getW().getData());
//                printArray("第"+(i+1)+"层的B",(double[])layer.getB().getData());
//            }
//            i++;
//        }
    }

    private static void saveFile(double[][][] image,String filePath){
        File file=new File(filePath);
        try {
            BufferedImage bufferedImage=double2Image(image);
            ImageIO.write(
                    bufferedImage,
                    "png",
                    new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage double2Image(double[][][] image){
        BufferedImage b = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);
        Graphics g = b.getGraphics();

        for(int n=0; n<image.length; n++) {
            for(int m=0; m<image[0].length; m++) {
                int v=(int)(image[n][m][0]*255);
                g.setColor(new Color(v, v, v));
                g.fillRect(m, n, 1, 1);
            }
        }
        return b;
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
