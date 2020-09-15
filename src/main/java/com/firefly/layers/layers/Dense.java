package com.firefly.layers.layers;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.core.OperationUnary;
import com.firefly.layers.core.Layer;
import com.firefly.layers.core.Neuron;
import com.firefly.layers.neuron.GeneralNeuron;

/**
 * 全连接层
 */
public class Dense implements Layer {
    private int inputs;
    private int units;
    private Class<? extends OperationUnary> activationCls;

    private Neuron[] out;

    public Dense(){

    }

    public Dense(int units,Class<? extends OperationUnary> activationCls){
        this.units=units;
        this.activationCls=activationCls;
    }

    public Dense(int inputs,int units,Class<? extends OperationUnary> activationCls){
        this.inputs=inputs;
        this.units=units;
        this.activationCls=activationCls;
    }

    public int getInputs() {
        return inputs;
    }

    public void setInputs(int inputs) {
        this.inputs = inputs;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    @Override
    public void init() {
        out=new Neuron[units];//输出单元数
        for(int i=0;i<units;i++){
            out[i]=new GeneralNeuron(inputs,activationCls);
            try {
                out[i].init();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public void calc(double[] input,double[] targetVal,double[] out) {
        for(int i=0;i<this.out.length;i++){
            out[i]=this.out[i].calc(input);
        }
    }

    @Override
    public void resetBackUpdateParamPrtGrad() {
        for(int i=0;i<this.out.length;i++){
            this.out[i].resetBackUpdateParamPrtGrad();
        }
    }

    @Override
    public void addBackUpdateParamPrtGrad(double[] prtGrad, double[] input,double[][] currentPrtGrad) {
        for(int i=0;i<this.out.length;i++){
            this.out[i].addBackUpdateParamPrtGrad(prtGrad,input,currentPrtGrad!=null?currentPrtGrad[i]:null);
        }
    }

    @Override
    public void flushBackUpdateParamPrtGrad(double rate) {
        for(int i=0;i<this.out.length;i++){
            this.out[i].flushBackUpdateParamPrtGrad(rate);
        }
    }

    @Override
    public void backUpdateInputPrtGrad(double[] prtGrad, double[] input, double[] outPrtGrad) {
        for(int i=0;i<this.out.length;i++){
            this.out[i].backUpdateInputPrtGrad(prtGrad,input,outPrtGrad);
        }
    }

}
