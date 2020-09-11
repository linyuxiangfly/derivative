package test;

import com.firefly.derivative.operation.Div;
import com.firefly.derivative.operation.Sin;
import com.firefly.derivative.operation.Var;

public class TestSin {
    public static void main(String[] args){
        //定义常量
        Var a=new Var(5);
        //sin函数
        Sin y=new Sin(a);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("y/a:"+y.der(y));
    }
}
