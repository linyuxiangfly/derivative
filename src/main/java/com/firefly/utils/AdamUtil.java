package com.firefly.utils;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;

/**
 * Adam优化器操作类
 */
public class AdamUtil {
    public static void calc(double[] prtGrad, double[] m, double[] v, double[] params,double rate,double beta1,double beta2,int iter) {
        iter++;
        double lrt  = rate * Math.sqrt(1.0 - Math.pow(beta2,iter)) / (1.0 - Math.pow(beta1,iter));

        for(int i=0;i<prtGrad.length;i++){
            double prtGradVal=prtGrad[i];
            double paramsVal=params[i];

            double mVal=m[i];
            double vVal=v[i];

            mVal+=(1-beta1)*(prtGradVal-mVal);
            vVal+=(1-beta2)*(prtGradVal*prtGradVal-vVal);

            paramsVal-=lrt*mVal/(Math.sqrt(vVal)+0.00000000001);

            m[i]=mVal;
            v[i]=vVal;
            params[i]=paramsVal;
        }
    }

    public static void calc(double[][] prtGrad, double[][] m, double[][] v, double[][] params,double rate,double beta1,double beta2,int iter) {
        iter++;
        double lrt  = rate * Math.sqrt(1.0 - Math.pow(beta2,iter)) / (1.0 - Math.pow(beta1,iter));

        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                double prtGradVal=prtGrad[i][j];
                double paramsVal=params[i][j];

                double mVal=m[i][j];
                double vVal=v[i][j];

                mVal+=(1-beta1)*(prtGradVal-mVal);
                vVal+=(1-beta2)*(prtGradVal*prtGradVal-vVal);

                paramsVal-=lrt*mVal/(Math.sqrt(vVal)+0.00000000001);

                m[i][j]=mVal;
                v[i][j]=vVal;
                params[i][j]=paramsVal;
            }
        }
    }

    public static void calc(double[][][] prtGrad, double[][][] m, double[][][] v, double[][][] params,double rate,double beta1,double beta2,int iter) {
        iter++;
        double lrt  = rate * Math.sqrt(1.0 - Math.pow(beta2,iter)) / (1.0 - Math.pow(beta1,iter));

        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                for(int k=0;k<prtGrad[i][j].length;k++){
                    double prtGradVal=prtGrad[i][j][k];
                    double paramsVal=params[i][j][k];

                    double mVal=m[i][j][k];
                    double vVal=v[i][j][k];

                    mVal+=(1-beta1)*(prtGradVal-mVal);
                    vVal+=(1-beta2)*(prtGradVal*prtGradVal-vVal);

                    paramsVal-=lrt*mVal/(Math.sqrt(vVal)+0.00000000001);

                    m[i][j][k]=mVal;
                    v[i][j][k]=vVal;
                    params[i][j][k]=paramsVal;
                }
            }
        }
    }

    public static void calc(double[][][][] prtGrad, double[][][][] m, double[][][][] v, double[][][][] params,double rate,double beta1,double beta2,int iter) {
        iter++;
        double lrt  = rate * Math.sqrt(1.0 - Math.pow(beta2,iter)) / (1.0 - Math.pow(beta1,iter));

        for(int i=0;i<prtGrad.length;i++){
            for(int j=0;j<prtGrad[i].length;j++){
                for(int k=0;k<prtGrad[i][j].length;k++){
                    for(int l=0;l<prtGrad[i][j][k].length;l++){
                        double prtGradVal=prtGrad[i][j][k][l];
                        double paramsVal=params[i][j][k][l];

                        double mVal=m[i][j][k][l];
                        double vVal=v[i][j][k][l];

                        mVal+=(1-beta1)*(prtGradVal-mVal);
                        vVal+=(1-beta2)*(prtGradVal*prtGradVal-vVal);

                        paramsVal-=lrt*mVal/(Math.sqrt(vVal)+0.00000000001);

                        m[i][j][k][l]=mVal;
                        v[i][j][k][l]=vVal;
                        params[i][j][k][l]=paramsVal;
                    }
                }
            }
        }
    }

    public static void calc(MultiDim prtGrad,MultiDim m,MultiDim v,MultiDim params,double rate,double beta1,double beta2,int iter){
        switch (prtGrad.getShape().howManyDim()) {
            case 1:
                calc((double[])prtGrad.getData(),(double[]) m.getData(),(double[]) v.getData(), (double[]) params.getData(),rate,beta1,beta2,iter);
                break;
            case 2:
                calc((double[][])prtGrad.getData(),(double[][]) m.getData(),(double[][]) v.getData(), (double[][]) params.getData(),rate,beta1,beta2,iter);
                break;
            case 3:
                calc((double[][][])prtGrad.getData(),(double[][][]) m.getData(),(double[][][]) v.getData(), (double[][][]) params.getData(),rate,beta1,beta2,iter);
                break;
            case 4:
                calc((double[][][][])prtGrad.getData(),(double[][][][]) m.getData(),(double[][][][]) v.getData(), (double[][][][]) params.getData(),rate,beta1,beta2,iter);
                break;
            default:
                iter++;
                double lrt  = rate * Math.sqrt(1.0 - Math.pow(beta2,iter)) / (1.0 - Math.pow(beta1,iter));

                ShapeIndex i=new ShapeIndex(prtGrad.getShape());
                do{
                    double prtGradVal=(double)prtGrad.getVal(i);
                    double paramsVal=(double)params.getVal(i);

                    double mVal=(double)m.getVal(i);
                    double vVal=(double)v.getVal(i);

                    mVal+=(1-beta1)*(prtGradVal-mVal);
                    vVal+=(1-beta2)*(prtGradVal*prtGradVal-vVal);

                    paramsVal-=lrt*mVal/(Math.sqrt(vVal)+0.00000000001);

                    m.setVal(i,mVal);
                    v.setVal(i,vVal);
                    params.setVal(i,paramsVal);
                }while (i.next());
                break;
        }
    }
}
