package com.firefly.layers.models;

import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Model;
import com.firefly.layers.core.Loss;

import java.util.ArrayList;
import java.util.List;

public class Sequential implements Model {
    private List<Layer> layers;
    private List<double[]> layersOut;
    private List<double[]> layersInputPrtGrad;//每层的识差/输入的梯度
    private Loss loss;
    private double[] lossOut;
    private double rate;

    private Class<? extends Loss> lossCls;

    public Sequential(double rate){
        this.rate=rate;
        layers=new ArrayList<>();
        layersOut=new ArrayList<>();
        layersInputPrtGrad=new ArrayList<>();
    }

    @Override
    public void add(Layer layer) {
        layers.add(layer);
    }

    @Override
    public void setLossCls(Class<? extends Loss> lossCls) {
        this.lossCls=lossCls;
    }

    @Override
    public void evaluate(double[][] x, double[][] y, int batchSize) {

    }

    @Override
    public double[] predict(double[] x) {
        return null;
    }

    @Override
    public void fit(double[][] x, double[][] y, int epoch, int batchSize) {
        int num=x.length/batchSize;
        int mod=x.length%batchSize;
        num+=mod>0?1:0;
        int lastSize=mod==0?batchSize:mod;

        //初始化层
        initLayers(layers,lossCls);

        //训练次数
        for(int en=0;en<epoch;en++){

            double lossVal=0;

            //分批训练，分成多少批
            for(int n=0;n<num;n++){
                //循环每批数据
                int bs=(n==num-1)?lastSize:batchSize;
                //偏移每批的的位置进行训练
                for(int i=n*batchSize;i<n*batchSize+bs;i++){
                    resetLayersInputPrtGrad();//重置每层的损失函数/输入参数偏导梯度
                    for(int li=0;li<layers.size();li++){
                        Layer layer=layers.get(li);
                        layer.resetBackUpdateParamPrtGrad();//重置临时梯度变量
                        if(li==0){
                            layer.calc(x[i],y[i],layersOut.get(li));
                        }else{
                            layer.calc(layersOut.get(li-1),y[i],layersOut.get(li));
                        }
                    }

                    double[] lastLayerOut=layersOut.get(layersOut.size()-1);

                    loss.calc(lastLayerOut,y[i],lossOut);
                    //累计识差
                    lossVal+=lossOut[0];

                    //损失函数/输入参数的梯度
                    double[] lossPrtGrad=loss.prtGrad(lastLayerOut,y[i]);
                    for(int li=layers.size()-1;li>=0;li--){
                        Layer layer=layers.get(li);

                        double[] currentInput=null;
                        if(li==0){
                            currentInput=x[i];
                        }else{
                            currentInput=layersOut.get(li-1);
                        }

                        if(li==layers.size()-1){
                            layer.addBackUpdateParamPrtGrad(lossPrtGrad,currentInput,layersInputPrtGrad.get(li));
                        }else{
                            layer.addBackUpdateParamPrtGrad(layersInputPrtGrad.get(li+1),currentInput,layersInputPrtGrad.get(li));
                        }
                    }

                    for(int li=0;li<layers.size();li++){
                        Layer layer=layers.get(li);
                        layer.flushBackUpdateParamPrtGrad(rate);
                    }
                }
            }

            System.out.println(String.format("%.10f", lossVal));
        }
    }

    private void initLayers(List<Layer> layers,Class<? extends Loss> lossCls){
        layersOut.clear();

        //初始化层
        int lastUnits=0;
        for(int i=0;i<layers.size();i++){
            Layer layer=layers.get(i);
            layersOut.add(new double[layer.getUnits()]);

            //第二层开始都是继承上层的输出个数
            if(i>0){
                layer.setInputs(lastUnits);
            }

            //定义每层的临时梯度
            if(i==0){
                layersInputPrtGrad.add(null);
            }else{
                layersInputPrtGrad.add(new double[layer.getInputs()]);
            }

            layer.init();
            lastUnits=layer.getUnits();//记录上一层的输出参数个数
        }

        try {
            loss=lossCls.newInstance();
            loss.setInputs(lastUnits);
            //创建输出数
            lossOut=new double[loss.getUnits()];
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 重置每层的损失函数/输入参数偏导梯度
     */
    private void resetLayersInputPrtGrad(){
        for(double[] datas:layersInputPrtGrad){
            if(datas!=null){
                for(int i=0;i<datas.length;i++){
                    datas[i]=0;
                }
            }
        }
    }
}
