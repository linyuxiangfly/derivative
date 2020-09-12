package test;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.operation.*;

public class TestDivMult {
    public static void main(String[] args){
        Var a=new Var(2);
        Var b=new Var(3);

        //定义常量
        Function[] params=new Function[]{
                new Const(7),
                new Mcl(a,b),
                new Div(a,b),
                new Const(4),
                new Const(5),
        };

        //多个数相除
        DivMult y=new DivMult(params);

        double yy=7.0/(2.0*3.0)/(2.0/3.0)/4.0/5.0;
        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        for(int i=0;i<params.length;i++){
            System.out.println("y/param["+i+"]:"+y.der(params[i]));
        }
        System.out.println("y/a:"+y.der(a));
        System.out.println("y/b:"+y.der(b));
    }
}
