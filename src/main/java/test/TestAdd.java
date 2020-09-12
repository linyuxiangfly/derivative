package test;

import com.firefly.derivative.operation.Add;
import com.firefly.derivative.operation.Const;

public class TestAdd {
    public static void main(String[] args){
        //定义常量
        Const a=new Const(5);
        Const b=new Const(4);
        //两个数相加
        Add y=new Add(a,b);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("y/a:"+y.prtGrad(a));
        System.out.println("y/b:"+y.prtGrad(b));
    }
}
