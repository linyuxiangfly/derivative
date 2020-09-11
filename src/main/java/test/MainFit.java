package test;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.operation.*;

public class MainFit {
    public static void main(String[] args){
        //二维数组，第1列是x,第二列是y
        double[][] datas=new double[][]{
                {32.3787591,32},
                {32.8252527,32.5},
                {33.3753966,33},
                {33.6624282,33.5},
                {33.9414867,34},
                {34.4039265,34.5},
                {34.7308236,35},
                {35.2331289,35.5},
                {35.5121874,36},
                {35.9188155,36.5},
                {36.5088249,37},
                {36.8038296,37.5},
                {36.9393723,38},
                {37.3539735,38.5},
                {38.0077677,39},
                {38.2629069,39.5},
                {38.749266,40},
                {39.1957596,40.5},
                {39.6342801,41},
                {40.0409082,41.5},
                {40.343886,42},
                {40.8701106,42.5},
                {41.2926849,43}
        };

        //将数据转成小数再进行计算
        one(datas);

        //更新率
        double rate=0.05;

        //x、y变量
        Var x=new Var(0);
        Var y=new Var(0);

        //多项式a、b、c 3个参数
        Var a=new Var(0.01);
        Var b=new Var(0.01);
        Var c=new Var(0.01);

        //生成多项式函数
        Function f=fx(x,a,b,c);

        //生成损失函数
        Function lf=lostFunc(f,y);

        //定义a、b、c 3个参数待更新的临时变量
        double ta,tb,tc;

        for(int i=0;i<10000;i++){
            //分别求lf/a、lf/b、lf/c的偏导函数在x和y处的值
            ta=rate*ds(datas,lf,y,x,a);
            tb=rate*ds(datas,lf,y,x,b);
            tc=rate*ds(datas,lf,y,x,c);

            //修改a、b、c参数的值
            a.setVal(a.getVal()-ta);
            b.setVal(b.getVal()-tb);
            c.setVal(c.getVal()-tc);
        }

        //输出a、b、c参数值
        System.out.println("a:"+a.getVal()+" b:"+b.getVal()+" c:"+c.getVal()+"\n");

        //使用a、b、c多项式参数对数据进行拟合
        for(int i=0;i<datas.length;i++){
            x.setVal(datas[i][0]);
            System.out.println("y:"+datas[i][1]*100+"   y':"+f.calc()*100);
        }
    }

    public static void one(double[][] datas){
        for(int x=0;x<datas.length;x++){
            for(int y=0;y<datas[x].length;y++){
                datas[x][y]=datas[x][y]/100;
            }
        }
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

//        Mcl bx=new Mcl(b,x);
//        Add bx_c=new Add(bx,c);
//        return bx_c;
    }

    /**
     * 求偏导
     * @param datas
     * @param lf
     * @param y
     * @param x
     * @param dx
     * @return
     */
    public static double ds(double[][] datas,Function lf,Var y,Var x,Var dx){
        double ret=0;
        for(double[] item:datas){
            x.setVal(item[0]);
            y.setVal(item[1]);
            //求导
            ret+=lf.der(dx);
        }
        return ret;
    }
}
