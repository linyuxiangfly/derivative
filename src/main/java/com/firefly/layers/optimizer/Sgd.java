package com.firefly.layers.optimizer;

import com.firefly.layers.core.Optimizer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;

/**
 * 随机梯度下降法
 */
public class Sgd implements Optimizer {
    private double rate;

    public Sgd(double rate){
        this.rate=rate;
    }
    @Override
    public void update(MultiDim params, MultiDim prtGrad) {
        switch (params.getShape().howManyDim()){
            case 1:
                update((double[])params.getData(),(double[])prtGrad.getData());
                break;
            case 2:
                update((double[][])params.getData(),(double[][])prtGrad.getData());
                break;
            case 3:
                update((double[][][])params.getData(),(double[][][])prtGrad.getData());
                break;
            case 4:
                update((double[][][][])params.getData(),(double[][][][])prtGrad.getData());
                break;
            default:
                ShapeIndex i=new ShapeIndex(params.getShape());
                do{
                    double val=(double)params.getVal(i);
                    val-=rate*(double)prtGrad.getVal(i);
                    params.setVal(i,val);
                }while (i.next());
                break;
        }
    }

    private void update(double[] params,double[] prtGrad){
        for(int i=0;i<params.length;i++){
            params[i]-=rate*prtGrad[i];
        }
    }

    private void update(double[][] params,double[][] prtGrad){
        for(int i=0;i<params.length;i++){
            for(int j=0;j<params[i].length;j++){
                params[i][j]-=rate*prtGrad[i][j];
            }
        }
    }

    private void update(double[][][] params,double[][][] prtGrad){
        for(int i=0;i<params.length;i++){
            for(int j=0;j<params[i].length;j++){
                for(int k=0;k<params[i][j].length;k++){
                    params[i][j][k]-=rate*prtGrad[i][j][k];
                }
            }
        }
    }

    private void update(double[][][][] params,double[][][][] prtGrad){
        for(int i=0;i<params.length;i++){
            for(int j=0;j<params[i].length;j++){
                for(int k=0;k<params[i][j].length;k++){
                    for(int l=0;l<params[i][j][k].length;l++){
                        params[i][j][k][l]-=rate*prtGrad[i][j][k][l];
                    }
                }
            }
        }
    }
}
