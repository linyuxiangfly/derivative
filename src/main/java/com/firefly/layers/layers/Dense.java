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
    private Neuron[] out;

    public Dense(){

    }

    public Dense(int inputs,int units,Class<? extends OperationUnary> activationCls){
        out=new Neuron[units];//输出单元数
        for(int i=0;i<units;i++){
            out[i]=new GeneralNeuron(inputs,activationCls);
        }
    }

    @Override
    public void calc(double[] input,double[] out) {
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
    public void addBackUpdateParamPrtGrad(double[] prtGrad, double[] input) {
        for(int i=0;i<this.out.length;i++){
            this.out[i].addBackUpdateParamPrtGrad(prtGrad,input);
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
