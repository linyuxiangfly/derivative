package com.firefly.derivative.util;

public class MathEx {
    public static double pow(double a,double b){
        //优化
        if(b==1){
            return a;
        }else{
            return Math.pow(a,b);
        }
    }

    public static double acot(double x){
        return Math.atan(1.0/x);
    }
    public static double cot(double x){
        return 1/Math.tan(x);
    }
    public static double sec(double x){
        return 1/Math.cos(x);
    }

    public static double csc(double x){
        return 1/Math.sin(x);
    }
}
