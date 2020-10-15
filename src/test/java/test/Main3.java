package test;

import com.firefly.derivative.activation.Sigmoid;
import com.firefly.derivative.activation.Tanh;
import com.firefly.derivative.core.Function;
import com.firefly.derivative.operation.*;

public class Main3 {
    public static void main(String[] args){
        //变量
        Var var=new Var(10);
        show("var",var,null);
        System.out.println();

        //加法
        Add add=new Add(new Var(5),new Var(8));
        show("add",add,add.getA());
        show("add",add,add.getB());
        System.out.println();

        //减法
        Sub sub=new Sub(new Var(5),new Var(8));
        show("sub",sub,sub.getA());
        show("sub",sub,sub.getB());
        System.out.println();

        //乘法
        Mcl mcl=new Mcl(new Var(5),new Var(8));
        show("mcl",mcl,mcl.getA());
        show("mcl",mcl,mcl.getB());
        System.out.println();

        //除法
        Div div=new Div(new Var(5),new Var(8));
        show("div",div,div.getA());
        show("div",div,div.getB());
        System.out.println();

        //指数、幂
        Power power=new Power(new Var(5),new Var(8));
        show("power",power,power.getA());
        show("power",power,power.getB());
        System.out.println();

        //sin
        Sin sin=new Sin(mcl);
        show("sin",sin,sin.getVal());
        System.out.println();

        //sigmoid
        Sigmoid sigmoid=new Sigmoid(new Var(0));
        show("sigmoid",sigmoid,sigmoid.getVal());
        System.out.println();

        //tanh
        Tanh tanh=new Tanh(new Var(1));
        show("tanh",tanh,tanh.getVal());
        System.out.println();
    }

    public static void show(String title,Function function,Function dx){
        System.out.println(title+",val:"+function.calc()+" derivative:"+function.prtGrad(dx));
    }
}
