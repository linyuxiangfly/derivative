package test;

import com.firefly.derivative.operation.Ln;
import com.firefly.derivative.operation.Sin;
import com.firefly.derivative.operation.Var;

public class TestLn {
    public static void main(String[] args){
        //定义常量
        Var a=new Var(5);
        //自然对数函数
        Ln y=new Ln(a);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("y/a:"+y.der(a));
    }
}
