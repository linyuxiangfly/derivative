package com.firefly.layers.optimizer;

import com.firefly.layers.core.Optimizer;
import com.firefly.layers.data.MultiDim;
import com.firefly.utils.AdaGradUtil;
import com.firefly.utils.MultiDimUtil;

import java.util.Hashtable;
import java.util.Map;

/**
 * 学习率衰减方法（learning rate decay），也就是说随着学习的进行，使学习率逐渐减少。AdaGrade 进一步发展了这个想法，它会为参数的每一个元素设当的调整学习率。
 */
public class AdaGrad implements Optimizer {
    private double rate;

    //历史梯度
    private Map<MultiDim,MultiDim> historyPrtGrad=new Hashtable<>();
    //临时用的
    private Map<MultiDim,MultiDim> temp=new Hashtable<>();

    public AdaGrad(double rate){
        this.rate=rate;
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
//        //临时变量
//        MultiDim temp=getTemp(params);
//        //历史梯度临时变量
//        MultiDim hTemp=getTemp(h);
//
//        //求梯度的平方
//        MultiDimUtil.mcl(prtGrad,prtGrad,temp);
//        //historyPrtGrad+=梯度的平方
//        MultiDimUtil.add(h,temp,h);
//
//        //sqrt(historyPrtGrad),平均开根
//        MultiDimUtil.sqrt(h,hTemp);
//        //另上一个极小的值
//        MultiDimUtil.add(hTemp,0.00000001,hTemp);
//
//        //temp=rate*梯度
//        MultiDimUtil.mcl(prtGrad,rate,temp);
//        //historyPrtGradTemp=decay*historyPrtGrad
//        MultiDimUtil.div(temp,hTemp,temp);
//
//        //参数=参数-历史梯度
//        MultiDimUtil.sub(params,temp,params);

        AdaGradUtil.calc(prtGrad,h,params,rate);
    }
}
