package com.firefly.layers.optimizer;

import com.firefly.layers.core.Optimizer;
import com.firefly.layers.data.MultiDim;
import com.firefly.utils.AdaGradUtil;
import com.firefly.utils.AdamUtil;

import java.util.Hashtable;
import java.util.Map;

/**
 * Adam 直观的来讲就是融合了 Momentum 和 AdaGrad 方法
 */
public class Adam implements Optimizer {
    private double rate;
    private double beta1;
    private double beta2;

    //参数对应的迭代
    private Map<MultiDim,Integer> iter=new Hashtable<>();
    //临时用的
    private Map<MultiDim,MultiDim> m=new Hashtable<>();
    private Map<MultiDim,MultiDim> v=new Hashtable<>();

    public Adam(double rate,double beta1,double beta2){
        this.rate=rate;
        this.beta1=beta1;
        this.beta2=beta2;
    }

    /**
     * 迭代
     * @param params
     * @return
     */
    private int getIter(MultiDim params){
        Integer ret=iter.get(params);
        if(ret==null){
            ret=0;
            iter.put(params,ret);
        }else{
            ret++;
            iter.put(params,ret);
        }
        return ret;
    }

    private MultiDim getM(MultiDim params){
        MultiDim vals=m.get(params);
        if(vals==null){
            vals=new MultiDim(params.getShape());
            m.put(params,vals);
        }
        return vals;
    }

    private MultiDim getV(MultiDim params){
        MultiDim vals=v.get(params);
        if(vals==null){
            vals=new MultiDim(params.getShape());
            v.put(params,vals);
        }
        return vals;
    }

    @Override
    public void update(MultiDim params, MultiDim prtGrad) {
        MultiDim m=getM(params);
        MultiDim v=getV(params);

        int iter=getIter(params);//迭代

        AdamUtil.calc(prtGrad,m,v,params,rate,beta1,beta2,iter);
    }
}
