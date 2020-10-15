package test;

import com.firefly.derivative.activation.Relu;
import com.firefly.layers.core.Model;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.layers.Dense;
import com.firefly.layers.layers.Dropout;
import com.firefly.layers.listeners.LossCallBackListener;
import com.firefly.layers.loss.Mse;
import com.firefly.layers.models.Sequential;

public class TestNNRelu {
    public static void main(String[] args){
        //二维数组，第1列是x,第二列是y
        double[][] datas=new double[][]{
                {32.3787591,32},
                {32.8252527,32.5},
                {33.3753966,33},
                {33.6624282,33.5},
                {33.9414867,34},
                {34.4039265,34.5},
                {34.7308236,35},
                {35.2331289,35.5},
                {35.5121874,36},
                {35.9188155,36.5},
                {36.5088249,37},
                {36.8038296,37.5},
                {36.9393723,38},
                {37.3539735,38.5},
                {38.0077677,39},
                {38.2629069,39.5},
                {38.749266,40},
                {39.1957596,40.5},
                {39.6342801,41},
                {40.0409082,41.5},
                {40.343886,42},
                {40.8701106,42.5},
                {41.2926849,43}
        };
        double[][] xx=new double[datas.length][1];
        double[][] yy=new double[datas.length][1];
        for(int i=0;i<datas.length;i++){
            xx[i][0]=datas[i][0]/100.0;
            yy[i][0]=datas[i][1]/100.0;
        }
        MultiDim[] x=arr2MultDim(xx);
        MultiDim[] y=arr2MultDim(yy);

        Model model=new Sequential(0.04);
        model.add(new Dense(1,1, Relu.class));
        model.add(new Dropout(0.5f));
        //识差函数
        model.setLossCls(Mse.class);
        model.init();

        model.fit(x, y, 10000, 20, new LossCallBackListener() {
            @Override
            public void onLoss(double val) {
                System.out.println(String.format("%.10f", val));
            }
        });

        for(int i=0;i<x.length;i++){
            MultiDim py=model.predict(x[i]);

            ShapeIndex j=new ShapeIndex(py.getShape());
            do{
                System.out.print(String.format("%.10f   ", (double)py.getVal(j)*100));
            }while(j.next());

            j=new ShapeIndex(y[i].getShape());
            do{
                System.out.print(String.format("%.10f   ", (double)y[i].getVal(j)*100));
            }while(j.next());

            System.out.println();
        }

    }

    private static MultiDim[] arr2MultDim(double[][] datas){
        MultiDim[] ret=new MultiDim[datas.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=new MultiDim(Double.TYPE,new Shape(new int[]{datas[i].length}),datas[i]);
        }
        return ret;
    }
}
