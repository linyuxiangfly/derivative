package test;

import com.firefly.derivative.operation.*;

public class TestM {
    public static void main(String[] args){
        //定义变量
        Var a=new Var(2);
        Var b=new Var(3);
        Var c=new Var(4);
        Var d=new Var(5);

        //组合复杂函数
        //u=a*b
        //v=c/d;
        //w=u-v;
        //y=sin(w)
        Mcl u=new Mcl(a,b);
        Div v=new Div(c,d);
        Sub w=new Sub(u,v);
        Sin y=new Sin(w);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("u/a:"+u.der(a));
        System.out.println("u/b:"+u.der(b));
        System.out.println("v/c:"+v.der(c));
        System.out.println("v/d:"+v.der(d));
        System.out.println("y/u:"+y.der(u));
        System.out.println("y/v:"+y.der(v));
        System.out.println("y/a:"+y.der(a));
        System.out.println("y/b:"+y.der(b));
        System.out.println("y/c:"+y.der(c));
        System.out.println("y/d:"+y.der(d));
    }
}
