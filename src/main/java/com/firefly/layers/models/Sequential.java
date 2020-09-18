package com.firefly.layers.models;

import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Model;
import com.firefly.layers.core.Loss;
import com.firefly.layers.listeners.FitControl;
import com.firefly.layers.listeners.LossCallBackListener;

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
    public void remove(Layer layer) {
        layers.remove(layer);
    }

    @Override
    public List<Layer> getLayers() {
        return layers;
    }

    @Override
    public void setLossCls(Class<? extends Loss> lossCls) {
        this.lossCls=lossCls;
    }

    @Override
    public double[] predict(double[] x) {
        //初始化层输出中间结果
        if(layers.size()!=layersOut.size()){
            initLayersOut();
        }

        for(int li=0;li<layers.size();li++){
            Layer layer=layers.get(li);
            if(li==0){
                layer.calc(x,layersOut.get(li));
            }else{
                layer.calc(layersOut.get(li-1),layersOut.get(li));
            }
        }
        return layersOut.get(layersOut.size()-1);
    }

    /**
     * 计算所有层的值
     * @param x
     * @return
     */
    private double[] calcAllLayers(double[] x){
        for(int li=0;li<layers.size();li++){
            Layer layer=layers.get(li);
            if(li==0){
                layer.calc(x,layersOut.get(li));
            }else{
                layer.calc(layersOut.get(li-1),layersOut.get(li));
            }
        }

        return layersOut.get(layersOut.size()-1);
    }

    /**
     * 计算反向修正参数
     * @param x 输入值
     * @param lossPrtGrad 损失函数/计算结果的梯度
     */
    private void calcBackPropagation(double[] x,double[] lossPrtGrad){
        for(int li=layers.size()-1;li>=0;li--){
            Layer layer=layers.get(li);

            double[] currentInput=null;
            if(li==0){
                currentInput=x;
            }else{
                currentInput=layersOut.get(li-1);
            }

            if(li==layers.size()-1){
                layer.addBackUpdateParamPrtGrad(lossPrtGrad,currentInput,layersInputPrtGrad.get(li));
            }else{
                layer.addBackUpdateParamPrtGrad(layersInputPrtGrad.get(li+1),currentInput,layersInputPrtGrad.get(li));
            }
        }
    }

    /**
     * 更新反向计算后的参数
     */
    private void flushBackPropagation(){
        for(int li=0;li<layers.size();li++){
            Layer layer=layers.get(li);
            layer.flushBackUpdateParamPrtGrad(rate);
        }
    }

    public void init(){
        //初始化层
        initLayers(layers,lossCls);
    }

    @Override
    public void fit(double[][] x, double[][] y, int epoch, int batchSize) {
        fit(x,y,epoch,batchSize,null);
    }

    @Override
    public void fit(double[][] x, double[][] y, int epoch, int batchSize, LossCallBackListener lossCallBackListener) {
        fit(x,y,epoch,batchSize,lossCallBackListener,null);
    }

    @Override
    public void fit(double[][] x, double[][] y, int epoch, int batchSize, LossCallBackListener lossCallBackListener, FitControl fitControl) {
        int num=x.length/batchSize;
        int mod=x.length%batchSize;
        num+=mod>0?1:0;
        int lastSize=mod==0?batchSize:mod;

        //训练次数
        for(int en=0;en<epoch;en++){
            //损失结果
            double lossVal=0;

            //分批训练，分成多少批
            for(int n=0;n<num;n++){
                //循环每批数据
                int bs=(n==num-1)?lastSize:batchSize;

                //重置每层的损失函数/输入参数偏导梯度
                resetLayersInputPrtGrad();
                //重置每层内部的临时偏导梯度变量
                resetBackUpdateParamPrtGrad();

                //偏移每批的的位置进行训练
                for(int i=n*batchSize;i<n*batchSize+bs;i++){
                    //计算所有层的值
                    double[] lastLayerOut=calcAllLayers(x[i]);

                    //如果要回调损失
                    if(lossCallBackListener!=null || fitControl!=null){
                        loss.calc(lastLayerOut,y[i],lossOut);
                        //累计识差
                        lossVal+=lossOut[0];
                    }

                    //损失函数/计算结果的梯度
                    double[] lossPrtGrad=loss.prtGrad(lastLayerOut,y[i]);

                    //计算反向修正参数
                    calcBackPropagation(x[i],lossPrtGrad);
                }

                //更新反向计算后的参数
                flushBackPropagation();
            }

            //回调损失
            if(lossCallBackListener!=null){
                lossCallBackListener.onLoss(lossVal);
            }
            //拟合过程控制
            if(fitControl!=null){
                //如果要停止则退出循环
                if(fitControl.onIsStop(en,lossVal)){
                    break;
                }
            }
        }
    }

    /**
     * 初始化层输出中间结果
     */
    private void initLayersOut(){
        layersOut.clear();
        for(int i=0;i<layers.size();i++){
            Layer layer=layers.get(i);
            layersOut.add(new double[layer.getUnits()]);
        }
    }

    private void initLayers(List<Layer> layers,Class<? extends Loss> lossCls){
        //初始化层输出中间结果
        initLayersOut();

        //初始化层
        int lastUnits=0;
        for(int i=0;i<layers.size();i++){
            Layer layer=layers.get(i);

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
            //创建输出数
            lossOut=new double[1];
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

    /**
     * 重置每层内部的临时偏导梯度变量
     */
    private void resetBackUpdateParamPrtGrad(){
        for(int li=0;li<layers.size();li++){
            Layer layer=layers.get(li);
            layer.resetBackUpdateParamPrtGrad();//重置每层内部的临时偏导梯度变量
        }
    }
}
