package test;

import com.firefly.derivative.operation.Div;
import com.firefly.derivative.operation.Mcl;
import com.firefly.derivative.operation.Var;

public class TestDiv {
    public static void main(String[] args){
        //定义常量
        Var a=new Var(5);
        Var b=new Var(4);
        //两个数相乘
        Div y=new Div(a,b);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("y/a:"+y.prtGrad(a));
        System.out.println("y/b:"+y.prtGrad(b));
    }
}
