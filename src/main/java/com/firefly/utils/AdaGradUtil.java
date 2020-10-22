package com.firefly.utils;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;

/**
 * AdaGrad优化器操作类
 */
public class AdaGradUtil {
    public static void calc(double[] prtGrad, double[] historyPrtGrad, double[] params,double rate) {
        for(int i=0;i<prtGrad.length;i++){
            double prtGradVal=prtGrad[i];

            historyPrtGrad[i]+=prtGradVal*prtGradVal;
            params[i]-=rate*prtGradVal/(Math.sqrt(historyPrtGrad[i])+0.00000000001);
        }
    }

    public static void calc(double[][] prtGrad, double[][] historyPrtGrad, double[][] params,double rate) {
        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                double prtGradVal=prtGrad[i][j];

                historyPrtGrad[i][j]+=prtGradVal*prtGradVal;
                params[i][j]-=rate*prtGradVal/(Math.sqrt(historyPrtGrad[i][j])+0.00000000001);
            }
        }
    }

    public static void calc(double[][][] prtGrad, double[][][] historyPrtGrad, double[][][] params,double rate) {
        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                for(int k=0;k<prtGrad[i][j].length;k++){
                    double prtGradVal=prtGrad[i][j][k];

                    historyPrtGrad[i][j][k]+=prtGradVal*prtGradVal;
                    params[i][j][k]-=rate*prtGradVal/(Math.sqrt(historyPrtGrad[i][j][k])+0.00000000001);
                }
            }
        }
    }

    public static void calc(double[][][][] prtGrad, double[][][][] historyPrtGrad, double[][][][] params,double rate) {
        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                for(int k=0;k<prtGrad[i][j].length;k++){
                    for(int l=0;l<prtGrad[i][j][k].length;l++){
                        double prtGradVal=prtGrad[i][j][k][l];

                        historyPrtGrad[i][j][k][l]+=prtGradVal*prtGradVal;
                        params[i][j][k][l]-=rate*prtGradVal/(Math.sqrt(historyPrtGrad[i][j][k][l])+0.00000000001);
                    }
                }
            }
        }
    }

    public static void calc(MultiDim prtGrad,MultiDim historyPrtGrad,MultiDim params,double rate){
        switch (prtGrad.getShape().howManyDim()) {
            case 1:
                calc((double[])prtGrad.getData(),(double[]) historyPrtGrad.getData(), (double[]) params.getData(),rate);
                break;
            case 2:
                calc((double[][])prtGrad.getData(),(double[][]) historyPrtGrad.getData(), (double[][]) params.getData(),rate);
                break;
            case 3:
                calc((double[][][])prtGrad.getData(),(double[][][]) historyPrtGrad.getData(), (double[][][]) params.getData(),rate);
                break;
            case 4:
                calc((double[][][][])prtGrad.getData(),(double[][][][]) historyPrtGrad.getData(), (double[][][][]) params.getData(),rate);
                break;
            default:
                ShapeIndex i=new ShapeIndex(prtGrad.getShape());
                do{
                    double paramsVal=(double)params.getVal(i);
                    double prtGradVal=(double)prtGrad.getVal(i);
                    double historyPrtGradVal=(double)historyPrtGrad.getVal(i);

                    historyPrtGradVal+=prtGradVal*prtGradVal;
                    historyPrtGrad.setVal(i,historyPrtGradVal);

                    paramsVal-=rate*prtGradVal/(Math.sqrt(historyPrtGradVal)+0.00000000001);

                    params.setVal(i,paramsVal);
                }while (i.next());
                break;
        }
    }
}
