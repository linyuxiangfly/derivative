package test;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.operation.*;

public class TestSigmoid {
    public static void main(String[] args){
        //定义常量
        Var x=new Var(3);

        Function y1=sigmoid(x);
        //计算结果
        System.out.println("y:"+y1.calc());
        //求偏导值
        System.out.println("y/a:"+y1.prtGrad(x));

        //sigmoid函数
        Sigmoid y=new Sigmoid(x);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("y/x:"+y.prtGrad(x));
    }

    private static Function sigmoid(Var x){
//        1/(1+Math.exp(-v));
        Mcl u=new Mcl(-1,x);
        Power exp=new Power(Math.E,u);
        Add add=new Add(1.0,exp);
        Div ret=new Div(1,add);
        return ret;
    }
}
