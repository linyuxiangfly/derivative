package test;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.operation.*;

public class Main2 {
    public static void main(String[] args){
        double[][] datas=new double[][]{
                {1,2},
                {2,4},
                {3,7},
                {4,12}
        };
        double rate=0.005;


        Var x=new Var(0);
        Var y=new Var(0);

        Var a=new Var(1);
        Var b=new Var(1);
        Var c=new Var(1);

        Function f=fx(x,a,b,c);
        Function lf=lostFunc(f,y);

        Var ta=new Var(a.getVal());
        Var tb=new Var(b.getVal());
        Var tc=new Var(c.getVal());

        double l;

        long startTime = System.currentTimeMillis();    //获取开始时间

        for(int i=0;i<10000;i++){
            //求偏导函数并更新参数
            ta.setVal(ta.getVal()-rate*ds(datas,lf,y,x,a));
            tb.setVal(tb.getVal()-rate*ds(datas,lf,y,x,b));
            tc.setVal(tc.getVal()-rate*ds(datas,lf,y,x,c));

            a.setVal(ta.getVal());
            b.setVal(tb.getVal());
            c.setVal(tc.getVal());

            //求误差
            l=lost(datas,lf,y,x);
            System.out.println("lost:"+String.format("%.5f",l)+"  a:"+a.getVal()+" b:"+b.getVal()+" c:"+c.getVal());
        }

        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

        for(int i=0;i<datas.length;i++){
            x.setVal(datas[i][0]);
            System.out.println("y:"+datas[i][1]+"   y':"+f.calc());
        }
    }

    public static double lost(double[][] datas,Function lostFunc,Var y,Var x){
        double l=0;
        for(double[] item:datas){
            x.setVal(item[0]);
            y.setVal(item[1]);
            l+=lostFunc.calc();
        }
        return l;
    }

    /**
     * 生成误差函数
     * @param fx
     * @param y
     * @return
     */
    public static Function lostFunc(Function fx,Var y){
        Const zeroPointFive=new Const(0.5);
        Const two=new Const(2);
        Sub diff=new Sub(y,fx);
        Power diff2=new Power(diff,two);
        Mcl ret=new Mcl(zeroPointFive,diff2);
        return ret;
    }

    /**
     * 生成函数
     * @param x
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static Function fx(Var x, Var a, Var b, Var c){
        Const two=new Const(2);
        Power x2=new Power(x,two);
        Mcl ax2=new Mcl(a,x2);
        Mcl bx=new Mcl(b,x);
        Add ax2_bx=new Add(ax2,bx);
        Add ax2_bx_c=new Add(ax2_bx,c);
        return ax2_bx_c;
    }

    public static double d(double[][] datas,Function lf,Var y,Var x,Function dx){
        double ret=0;
        for(double[] item:datas){
            x.setVal(item[0]);
            y.setVal(item[1]);
            //偏导梯度
            ret+=lf.prtGrad(dx);
        }
        return ret;
    }

    public static double ds(double[][] datas,Function lf,Var y,Var x,Var dx){
        double ret=0;
        ret=d(datas,lf,y,x,dx);
        return ret;
    }
}
