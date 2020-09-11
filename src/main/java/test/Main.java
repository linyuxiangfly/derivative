package test;

public class Main {
    public static void main(String[] args){
        double[][] datas=new double[][]{
                {32.3787591,32},
                {32.8252527,32.5},
                {33.3753966,33},
                {33.6624282,33.5},
                {33.9414867,34},
                {34.4039265,34.5},
                {34.7308236,35},
                {35.2331289,35.5},
                {35.5121874,36},
                {35.9188155,36.5},
                {36.5088249,37},
                {36.8038296,37.5},
                {36.9393723,38},
                {37.3539735,38.5},
                {38.0077677,39},
                {38.2629069,39.5},
                {38.749266,40},
                {39.1957596,40.5},
                {39.6342801,41},
                {40.0409082,41.5},
                {40.343886,42},
                {40.8701106,42.5},
                {41.2926849,43}
        };

        one(datas);

        double rate=0.05;

        double a=0.01,b=0.01,c=0.01;
        double ta,tb,tc;
        double l;

        long startTime = System.currentTimeMillis();    //获取开始时间

        for(int i=0;i<100000;i++){
            ta=rate*das(datas,a,b,c);
            tb=rate*dbs(datas,a,b,c);
            tc=rate*dcs(datas,a,b,c);

            a-=ta;
            b-=tb;
            c-=tc;

//            l=lost(datas,a,b,c);
//            System.out.println(i+"  lost:"+String.format("%.5f",l)+"  a:"+a+" b:"+b+" c:"+c);
        }

        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

        for(int i=0;i<datas.length;i++){
            System.out.println("y:"+datas[i][1]+"   y':"+fx(datas[i][0],a,b,c));
        }
    }

    public static void one(double[][] datas){
        for(int x=0;x<datas.length;x++){
            for(int y=0;y<datas[x].length;y++){
                datas[x][y]=datas[x][y]/100;
            }
        }
    }

    public static void src(double[][] datas){
        for(int x=0;x<datas.length;x++){
            for(int y=0;y<datas[x].length;y++){
                datas[x][y]=datas[x][y]*100;
            }
        }
    }

    public static double lost(double[][] datas,double a,double b,double c){
        double l=0;
        for(double[] item:datas){
            double diff=item[1]-fx(item[0],a,b,c);
            l+=0.5*diff*diff;
        }
        return l;
    }

    public static double fx(double x,double a,double b,double c){
        return a*x*x+b*x+c;
    }

    public static double das(double[][] datas,double a,double b,double c){
        double ret=0;
        for(double[] item:datas){
            ret+=da(item[1],fx(item[0],a,b,c),item[0]);
        }
        return ret;
    }

    public static double da(double y,double ty,double x){
        return (ty-y)*x*x;
    }

    public static double dbs(double[][] datas,double a,double b,double c){
        double ret=0;
        for(double[] item:datas){
            ret+=db(item[1],fx(item[0],a,b,c),item[0]);
        }
        return ret;
    }

    public static double db(double y,double ty,double x){
        return (ty-y)*x;
    }

    public static double dcs(double[][] datas,double a,double b,double c){
        double ret=0;
        for(double[] item:datas){
            ret+=dc(item[1],fx(item[0],a,b,c),item[0]);
        }
        return ret;
    }

    public static double dc(double y,double ty,double x){
        return ty-y;
    }
}
