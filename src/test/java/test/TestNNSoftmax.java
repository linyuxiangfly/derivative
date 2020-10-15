package test;

import com.firefly.derivative.activation.Softmax;
import com.firefly.layers.core.Model;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.layers.init.params.InitParamsRandomGaussian;
import com.firefly.layers.layers.Dense;
import com.firefly.layers.listeners.LossCallBackListener;
import com.firefly.layers.loss.Cel;
import com.firefly.layers.loss.Mse;
import com.firefly.layers.models.Sequential;

public class TestNNSoftmax {
    public static void main(String[] args){
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

        Model model=new Sequential(0.0001);
        model.add(new Dense(12,2, Softmax.class,new InitParamsRandomGaussian()));//使用softmax激活函数
        //识差函数
        model.setLossCls(Cel.class);
        model.init();

        model.fit(x, y, 10000, 10, new LossCallBackListener() {
            @Override
            public void onLoss(double val) {
                System.out.println(String.format("%.10f", val));
            }
        });

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

    }

    private static MultiDim[] arr2MultDim(double[][] datas){
        MultiDim[] ret=new MultiDim[datas.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=new MultiDim(Double.TYPE,new Shape(new int[]{datas[i].length}),datas[i]);
        }
        return ret;
    }
}
