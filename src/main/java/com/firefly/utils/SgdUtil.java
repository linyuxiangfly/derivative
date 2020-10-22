package com.firefly.utils;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;

/**
 * Momentum优化器操作类
 */
public class SgdUtil {
    public static void calc(double[] prtGrad, double[] params,double rate) {
        for(int i=0;i<prtGrad.length;i++){
            double val=params[i]-rate*prtGrad[i];
            params[i]=val;
        }
    }

    public static void calc(double[][] prtGrad, double[][] params,double rate) {
        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                double val=params[i][j]-rate*prtGrad[i][j];
                params[i][j]=val;
            }
        }
    }

    public static void calc(double[][][] prtGrad, double[][][] params,double rate) {
        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                for(int k=0;k<prtGrad[i][j].length;k++){
                    double val=params[i][j][k]-rate*prtGrad[i][j][k];
                    params[i][j][k]=val;
                }
            }
        }
    }

    public static void calc(double[][][][] prtGrad, double[][][][] params,double rate) {
        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                for(int k=0;k<prtGrad[i][j].length;k++){
                    for(int l=0;l<prtGrad[i][j][k].length;l++){
                        double val=params[i][j][k][l]-rate*prtGrad[i][j][k][l];
                        params[i][j][k][l]=val;
                    }
                }
            }
        }
    }

    public static void calc(MultiDim prtGrad,MultiDim params,double rate){
        switch (prtGrad.getShape().howManyDim()) {
            case 1:
                calc((double[])prtGrad.getData(), (double[]) params.getData(),rate);
                break;
            case 2:
                calc((double[][])prtGrad.getData(),(double[][]) params.getData(),rate);
                break;
            case 3:
                calc((double[][][])prtGrad.getData(),(double[][][]) params.getData(),rate);
                break;
            case 4:
                calc((double[][][][])prtGrad.getData(),(double[][][][]) params.getData(),rate);
                break;
            default:
                ShapeIndex i=new ShapeIndex(prtGrad.getShape());
                do{
                    double paramsVal=(double)params.getVal(i);

                    paramsVal-=(rate*(double)prtGrad.getVal(i));;
                    params.setVal(i,paramsVal);
                }while (i.next());
                break;
        }
    }
}
