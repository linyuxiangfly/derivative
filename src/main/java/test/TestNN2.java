package test;

import com.firefly.derivative.operation.Relu;
import com.firefly.derivative.operation.Sigmoid;
import com.firefly.layers.core.Model;
import com.firefly.layers.layers.Dense;
import com.firefly.layers.listeners.LossCallBackListener;
import com.firefly.layers.loss.Mse;
import com.firefly.layers.models.Sequential;

public class TestNN2 {
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
        double[][] x=new double[datas.length][1];
        double[][] y=new double[datas.length][1];
        for(int i=0;i<datas.length;i++){
            x[i][0]=datas[i][0]/100.0;
            y[i][0]=datas[i][1]/100.0;
        }

        Model model=new Sequential(0.007);
        model.add(new Dense(1,1, Relu.class));
//        model.add(new Dense(1, Sigmoid.class));
        //识差函数
        model.setLossCls(Mse.class);

        model.fit(x, y, 100000, 20, new LossCallBackListener() {
            @Override
            public void onLoss(double val) {
                System.out.println(String.format("%.10f", val));
            }
        });

        for(int i=0;i<x.length;i++){
            double[] py=model.predict(x[i]);
            System.out.print(String.format("%.10f   ", py[0]));
            System.out.print(String.format("%.10f   ", y[i][0]));
            System.out.println();
        }

    }
}
