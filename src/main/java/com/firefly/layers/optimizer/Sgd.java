package com.firefly.layers.optimizer;

import com.firefly.layers.core.Optimizer;
import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.ShapeIndex;
import com.firefly.utils.MultiDimUtil;
import com.firefly.utils.SgdUtil;

import java.util.Hashtable;
import java.util.Map;

/**
 * 随机梯度下降法
 */
public class Sgd implements Optimizer {
    private static final long serialVersionUID = 1L;

    private double rate;
    //临时用的
    private Map<MultiDim,MultiDim> temp=new Hashtable<>();

    public Sgd(double rate){
        this.rate=rate;
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
        //临时变量
        MultiDim temp=getTemp(params);
        //temp=rate*梯度
        MultiDimUtil.mcl(prtGrad,rate,temp);
        //参数=参数-历史梯度
        MultiDimUtil.sub(params,temp,params);
//        SgdUtil.calc(prtGrad,params,rate);
    }
}
