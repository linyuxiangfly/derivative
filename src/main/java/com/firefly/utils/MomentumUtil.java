package com.firefly.utils;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;

/**
 * Momentum优化器操作类
 */
public class MomentumUtil {
    public static void calc(double[] prtGrad, double[] historyPrtGrad, double[] params,double rate,double decay) {
        for(int i=0;i<prtGrad.length;i++){
            historyPrtGrad[i]=(rate*prtGrad[i])-(decay*historyPrtGrad[i]);
            params[i]-=historyPrtGrad[i];
        }
    }

    public static void calc(double[][] prtGrad, double[][] historyPrtGrad, double[][] params,double rate,double decay) {
        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                historyPrtGrad[i][j]=(rate*prtGrad[i][j])-(decay*historyPrtGrad[i][j]);
                params[i][j]-=historyPrtGrad[i][j];
            }
        }
    }

    public static void calc(double[][][] prtGrad, double[][][] historyPrtGrad, double[][][] params,double rate,double decay) {
        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                for(int k=0;k<prtGrad[i][j].length;k++){
                    historyPrtGrad[i][j][k]=(rate*prtGrad[i][j][k])-(decay*historyPrtGrad[i][j][k]);
                    params[i][j][k]-=historyPrtGrad[i][j][k];
                }
            }
        }
    }

    public static void calc(double[][][][] prtGrad, double[][][][] historyPrtGrad, double[][][][] params,double rate,double decay) {
        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                for(int k=0;k<prtGrad[i][j].length;k++){
                    for(int l=0;l<prtGrad[i][j][k].length;l++){
                        historyPrtGrad[i][j][k][l]=(rate*prtGrad[i][j][k][l])-(decay*historyPrtGrad[i][j][k][l]);
                        params[i][j][k][l]-=historyPrtGrad[i][j][k][l];
                    }
                }
            }
        }
    }

    public static void calc(MultiDim prtGrad,MultiDim historyPrtGrad,MultiDim params,double rate,double decay){
        switch (prtGrad.getShape().howManyDim()) {
            case 1:
                calc((double[])prtGrad.getData(),(double[]) historyPrtGrad.getData(), (double[]) params.getData(),rate,decay);
                break;
            case 2:
                calc((double[][])prtGrad.getData(),(double[][]) historyPrtGrad.getData(), (double[][]) params.getData(),rate,decay);
                break;
            case 3:
                calc((double[][][])prtGrad.getData(),(double[][][]) historyPrtGrad.getData(), (double[][][]) params.getData(),rate,decay);
                break;
            case 4:
                calc((double[][][][])prtGrad.getData(),(double[][][][]) historyPrtGrad.getData(), (double[][][][]) params.getData(),rate,decay);
                break;
            default:
                ShapeIndex i=new ShapeIndex(prtGrad.getShape());
                do{
                    double paramsVal=(double)params.getVal(i);
                    double historyPrtGradVal=(double)historyPrtGrad.getVal(i);

                    double diff=(rate*(double)prtGrad.getVal(i))-(decay*historyPrtGradVal);
                    historyPrtGrad.setVal(i,diff);

                    paramsVal-=diff;

                    params.setVal(i,paramsVal);
                }while (i.next());
                break;
        }
    }
}
