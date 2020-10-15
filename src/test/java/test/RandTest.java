package test;

import com.firefly.math.Binomial;

public class RandTest {
    public static void main(String[] args){
        int[] val= Binomial.binomialOfInt(0.8f,1000);

        float tnum=0,fnum=0;
        for(int i=0;i<val.length;i++){
            if(val[i]==1){
                tnum++;
            }else{
                fnum++;
            }

            System.out.println(val[i]);
        }
        System.out.println("rate:"+tnum/(tnum+fnum));
    }


}
