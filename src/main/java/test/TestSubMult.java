package test;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.operation.*;

public class TestSubMult {
    public static void main(String[] args){
        Var a=new Var(4);
        Var b=new Var(5);

        //定义常量
        Function[] params=new Function[]{
                new Const(1),
                new Mcl(a,b),
                new Div(a,b),
                new Const(4),
                new Const(5),
        };

        //多个数相减
        SubMult y=new SubMult(params);

        //计算结果
        System.out.println("y:"+y.calc());

        //求偏导值
        for(int i=0;i<params.length;i++){
            System.out.println("y/param["+i+"]:"+y.prtGrad(params[i]));
        }
        System.out.println("y/a:"+y.prtGrad(a));
        System.out.println("y/b:"+y.prtGrad(b));
    }
}
