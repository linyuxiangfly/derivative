package test;

import com.firefly.derivative.operation.Power;
import com.firefly.derivative.operation.Sin;
import com.firefly.derivative.operation.Var;

public class TestPower {
    public static void main(String[] args){
        //定义常量
        Var a=new Var(5);
        Var b=new Var(3);
        //指数、幂函数
        Power y=new Power(a,b);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("y/a:"+y.der(a));
        System.out.println("y/b:"+y.der(b));
    }
}
