package com.firefly.layers.optimizer;

import com.firefly.layers.core.Optimizer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.utils.MomentumUtil;
import com.firefly.utils.MultiDimUtil;

import java.util.Hashtable;
import java.util.Map;

/**
 * Momentum 在 SGD 的基础上引入了一个变量 - 速度 v 和一个超参数 - 指数衰减平均 \alpha
 */
public class Momentum implements Optimizer {
    private double rate;
    private double decay;

    //历史梯度
    private Map<MultiDim,MultiDim> historyPrtGrad=new Hashtable<>();
    //临时用的
    private Map<MultiDim,MultiDim> temp=new Hashtable<>();

    public Momentum(double rate,double decay){
        this.rate=rate;
        this.decay=decay;
    }

    /**
     * 通过参数获取历史变更梯度
     * @param params
     * @return
     */
    private MultiDim getHistoryPrtGrad(MultiDim params){
        MultiDim prtGrad=historyPrtGrad.get(params);
        if(prtGrad==null){
            prtGrad=new MultiDim(params.getShape());
            historyPrtGrad.put(params,prtGrad);
        }
        return prtGrad;
    }

    private MultiDim getTemp(MultiDim params){
        MultiDim vals=temp.get(params);
        if(vals==null){
            vals=new MultiDim(params.getShape());
            temp.put(params,vals);
        }
        return vals;
    }

    @Override
    public void update(MultiDim params, MultiDim prtGrad) {
        MultiDim historyPrtGrad=getHistoryPrtGrad(params);
//        //临时变量
//        MultiDim temp=getTemp(params);
//        //历史梯度临时变量
//        MultiDim historyPrtGradTemp=getTemp(historyPrtGrad);
//
//        //temp=rate*梯度
//        MultiDimUtil.mcl(prtGrad,rate,temp);
//        //historyPrtGradTemp=decay*historyPrtGrad
//        MultiDimUtil.mcl(historyPrtGrad,decay,historyPrtGradTemp);
//
//        //历史梯度=梯度-历史梯度
//        MultiDimUtil.sub(temp,historyPrtGradTemp,historyPrtGrad);
//        //参数=参数-历史梯度
//        MultiDimUtil.sub(params,historyPrtGrad,params);
        MomentumUtil.calc(prtGrad,historyPrtGrad,params,rate,decay);
    }
}
