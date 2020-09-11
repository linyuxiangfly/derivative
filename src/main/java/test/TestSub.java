package test;

import com.firefly.derivative.operation.Add;
import com.firefly.derivative.operation.Const;
import com.firefly.derivative.operation.Sub;
import com.firefly.derivative.operation.Var;

public class TestSub {
    public static void main(String[] args){
        //定义常量
        Var a=new Var(5);
        Const b=new Const(4);
        //两个数相减
        Sub y=new Sub(a,b);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("y/a:"+y.der(a));
        System.out.println("y/b:"+y.der(b));
    }
}
