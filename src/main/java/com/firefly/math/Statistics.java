package com.firefly.math;

/**
 * 统计类
 */
public class Statistics {
    /**
     * 求和
     * @param vals
     * @return
     */
    public static double sum(double[] vals){
        double ret=0;
        for(int i=0;i<vals.length;i++){
            ret+=vals[i];
        }
        return ret;
    }
}
