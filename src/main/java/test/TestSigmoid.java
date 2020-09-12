package test;

import com.firefly.derivative.operation.Ln;
import com.firefly.derivative.operation.Sigmoid;
import com.firefly.derivative.operation.Var;

public class TestSigmoid {
    public static void main(String[] args){
        //定义常量
        Var a=new Var(5);
        //sigmoid函数
        Sigmoid y=new Sigmoid(a);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("y/a:"+y.prtGrad(a));
    }
}
