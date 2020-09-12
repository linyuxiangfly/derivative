package test;

import com.firefly.derivative.operation.Const;
import com.firefly.derivative.operation.Mcl;
import com.firefly.derivative.operation.Sub;
import com.firefly.derivative.operation.Var;

public class TestMcl {
    public static void main(String[] args){
        //定义常量
        Var a=new Var(5);
        Var b=new Var(4);
        //两个数相除
        Mcl y=new Mcl(a,b);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        System.out.println("y/a:"+y.prtGrad(a));
        System.out.println("y/b:"+y.prtGrad(b));
    }
}
