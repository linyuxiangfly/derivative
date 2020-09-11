package test;

import com.firefly.derivative.core.Function;
import com.firefly.derivative.operation.*;

public class MainVerify {
    public static void main(String[] args){
        x3();

        uv1();

        uv2();

        uv3();

        uvwxz();

        sin();
    }

    private static void x3(){
        System.out.println("x3##############################################");
        //y=x^3
        Var x=new Var(4);
        Mcl xx=new Mcl(x,x);
        Mcl xxx=new Mcl(xx,x);

        show("xx",xx,xx.getA(),x.getVal()*x.getVal(),2*x.getVal());
        show("xx",xx,xx.getB(),x.getVal()*x.getVal(),2*x.getVal());
        System.out.println();

        show("xxx",xxx,xxx.getA(),xxx.getA().calc()*xxx.getB().calc(),xxx.getB().calc());
        show("xxx",xxx,xxx.getB(),xxx.getA().calc()*xxx.getB().calc(),3*x.getVal()*x.getVal());
        System.out.println();
    }

    private static void uv1(){
        System.out.println("uv1##############################################");
        //y=u*v,u=a*b
        Var a=new Var(4);
        Var b=new Var(5);
        Mcl u=new Mcl(a,b);

        Var v=new Var(6);

        Mcl y=new Mcl(u,v);

        show("u",u,a,u.getA().calc()*u.getB().calc(),u.getB().calc());
        show("u",u,b,u.getA().calc()*u.getB().calc(),u.getA().calc());
        System.out.println();

        show("y",y,u,a.getVal()*b.getVal()*v.getVal(),v.getVal());
        show("y",y,v,a.getVal()*b.getVal()*v.getVal(),u.calc());
        show("y",y,a,a.getVal()*b.getVal()*v.getVal(),v.getVal()*b.getVal());
        show("y",y,b,a.getVal()*b.getVal()*v.getVal(),v.getVal()*a.getVal());
        System.out.println();
    }

    private static void uv2(){
        System.out.println("uv2##############################################");
        //y=u*v,u=a/b,v=c+d
        Var a=new Var(4);
        Var b=new Var(5);
        Var c=new Var(6);
        Var d=new Var(7);

        Div u=new Div(a,b);

        Add v=new Add(c,d);

        Mcl y=new Mcl(u,v);

        show("u",u,a,a.getVal()/b.getVal(),1/b.getVal());
        show("u",u,b,a.getVal()/b.getVal(),-a.getVal()/(b.getVal()*b.getVal()));
        System.out.println();

        show("v",v,c,c.getVal()+d.getVal(),1);
        show("v",v,d,c.getVal()+d.getVal(),1);
        System.out.println();

        show("y",y,u,(a.getVal()/b.getVal())*(c.getVal()+d.getVal()),v.calc());
        show("y",y,v,(a.getVal()/b.getVal())*(c.getVal()+d.getVal()),u.calc());
        show("y",y,a,(a.getVal()/b.getVal())*(c.getVal()+d.getVal()),v.calc()*(1/b.getVal()));
        show("y",y,b,(a.getVal()/b.getVal())*(c.getVal()+d.getVal()),v.calc()*-a.getVal()/(b.getVal()*b.getVal()));
        show("y",y,c,(a.getVal()/b.getVal())*(c.getVal()+d.getVal()),u.calc());
        show("y",y,d,(a.getVal()/b.getVal())*(c.getVal()+d.getVal()),u.calc());
        System.out.println();
    }

    private static void uv3(){
        System.out.println("uv3##############################################");
        //y=u*v,u=a/b,v=c-d
        Var a=new Var(4);
        Var b=new Var(5);
        Var c=new Var(6);
        Var d=new Var(7);

        Div u=new Div(a,b);

        Sub v=new Sub(c,d);

        Mcl y=new Mcl(u,v);

        show("u",u,a,a.getVal()/b.getVal(),1/b.getVal());
        show("u",u,b,a.getVal()/b.getVal(),-a.getVal()/(b.getVal()*b.getVal()));
        System.out.println();

        show("v",v,c,c.getVal()-d.getVal(),1);
        show("v",v,d,c.getVal()-d.getVal(),-1);
        System.out.println();

        show("y",y,u,(a.getVal()/b.getVal())*(c.getVal()-d.getVal()),v.calc());
        show("y",y,v,(a.getVal()/b.getVal())*(c.getVal()-d.getVal()),u.calc());
        show("y",y,a,(a.getVal()/b.getVal())*(c.getVal()-d.getVal()),v.calc()*(1/b.getVal()));
        show("y",y,b,(a.getVal()/b.getVal())*(c.getVal()-d.getVal()),v.calc()*-a.getVal()/(b.getVal()*b.getVal()));
        show("y",y,c,(a.getVal()/b.getVal())*(c.getVal()-d.getVal()),u.calc());
        show("y",y,d,(a.getVal()/b.getVal())*(c.getVal()-d.getVal()),-u.calc());
        System.out.println();
    }

    private static void uvwxz(){
        System.out.println("uvwxz##############################################");
        //u=a+b,v=c-d,w=a*b,x=c/d,z=w*x
        //y=u=v-z
        Var a=new Var(4);
        Var b=new Var(5);
        Var c=new Var(6);
        Var d=new Var(7);

        Add u=new Add(a,b);
        Sub v=new Sub(c,d);
        Mcl w=new Mcl(a,b);
        Div x=new Div(c,d);
        Mcl z=new Mcl(w,x);
        Add y1=new Add(u,v);
        Sub y=new Sub(y1,z);

        show("u",u,a,a.getVal()+b.getVal(),1);
        show("u",u,b,a.getVal()+b.getVal(),1);
        System.out.println();

        show("v",v,c,c.getVal()-d.getVal(),1);
        show("v",v,d,c.getVal()-d.getVal(),-1);
        System.out.println();

        show("w",w,a,a.getVal()*b.getVal(),b.getVal());
        show("w",w,b,a.getVal()*b.getVal(),a.getVal());
        System.out.println();

        show("x",x,c,c.getVal()/d.getVal(),1/d.getVal());
        show("x",x,d,c.getVal()/d.getVal(),-c.getVal()/(d.getVal()*d.getVal()));
        System.out.println();

        show("z",z,w,w.calc()*x.calc(),x.calc());
        show("z",z,x,w.calc()*x.calc(),w.calc());
        System.out.println();

        show("y",y,u,y.calc(),1);
        show("y",y,v,y.calc(),1);
        show("y",y,z,y.calc(),-1);

        show("y",y,a,y.calc(),1+(-1)*x.calc()*b.calc());
        show("y",y,b,y.calc(),1+(-1)*x.calc()*a.calc());
        show("y",y,c,y.calc(),1+(-1)*w.calc()*(1/d.calc()));
        show("y",y,d,y.calc(),(-1)+(-1)*w.calc()*-c.calc()/(d.calc()*d.calc()));
    }

    private static void sin(){
        System.out.println("sin##############################################");
        //u=a*b,v=a/b,w=u+v
        //y=sin(w)
        Var a=new Var(4);
        Var b=new Var(5);
        Mcl u=new Mcl(a,b);
        Div v=new Div(a,b);
        Add w=new Add(u,v);
        Sin y=new Sin(w);

        show("u",u,a,a.getVal()*b.getVal(),b.getVal());
        show("u",u,b,a.getVal()*b.getVal(),a.getVal());
        System.out.println();

        show("v",v,a,a.getVal()/b.getVal(),1/b.getVal());
        show("v",v,b,a.getVal()/b.getVal(),-a.getVal()/(b.getVal()*b.getVal()));
        System.out.println();

        show("w",w,u,u.calc()+v.calc(),1);
        show("w",w,v,u.calc()+v.calc(),1);
        show("w",w,a,u.calc()+v.calc(),b.getVal()+(1/b.getVal()));
        show("w",w,b,u.calc()+v.calc(),a.getVal()+(-a.getVal())/(b.getVal()*b.getVal()));
        System.out.println();

        show("y",y,w,Math.sin(w.calc()),Math.cos(w.calc()));
        show("y",y,u,Math.sin(w.calc()),Math.cos(w.calc()));
        show("y",y,v,Math.sin(w.calc()),Math.cos(w.calc()));
        show("y",y,a,Math.sin(w.calc()),Math.cos(w.calc())*(b.getVal()+(1/b.getVal())));
        show("y",y,b,Math.sin(w.calc()),Math.cos(w.calc())*(a.getVal()+(-a.getVal())/(b.getVal()*b.getVal())));
        System.out.println();
    }

    public static void show(String title,Function function,Function dx,double verifyCalc,double verifyDer){
        double calc=function.calc();
        double der=function.der(dx);

        if(calc==verifyCalc && der==verifyDer){
            System.out.println(title+",calc:"+calc+" der:"+der);
        }else{
            System.out.println(title+",calc:"+calc+" verifyCalc:"+verifyCalc+" der:"+der+"   verifyDer:"+verifyDer+"            error!");
        }
    }
}
