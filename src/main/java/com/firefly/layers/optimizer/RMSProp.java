package com.firefly.layers.optimizer;

import com.firefly.layers.core.Optimizer;
import com.firefly.layers.data.MultiDim;
import com.firefly.utils.RMSPropUtil;

import java.util.Hashtable;
import java.util.Map;

/**
 * RMSprop（即均方根传播）是由Geoff Hinton开发的，
 * 如《An Overview of Gradient Descent Optimization Algorithms》所述，
 * 其目的是解决AdaGrad的学习率急剧下降的问题。
 * 简而言之，RMSprop更改学习速率的速度比AdaGrad慢，
 * 但是RMSprop仍可从AdaGrad（更快的收敛速度）中受益
 */
public class RMSProp implements Optimizer {
    private static final long serialVersionUID = 1L;

    private double rate;
    private double decay=0.9;

    //历史梯度
    private Map<MultiDim,MultiDim> historyPrtGrad=new Hashtable<>();
    //临时用的
    private Map<MultiDim,MultiDim> temp=new Hashtable<>();

    public RMSProp(double rate){
        this(rate,0.9);
    }

    public RMSProp(double rate,double decay){
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
        //获取历史变量
        MultiDim h=getHistoryPrtGrad(params);

        RMSPropUtil.calc(prtGrad,h,params,rate,decay);
    }
}
