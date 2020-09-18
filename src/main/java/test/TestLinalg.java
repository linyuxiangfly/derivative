package test;

import com.firefly.math.Linalg;

public class TestLinalg {
    public static void main(String[] args){
        double[] ia=new double[]{1,2,3};
        double[] ib=new double[]{4,5,6};
        System.out.println(Linalg.inner(ia,ib)+"\n");

        double[] ic=Linalg.add(ia,ib);
        for(int i=0;i<ic.length;i++){
            System.out.println(ic[i]);
        }

        double[][] ia2=new double[][]{
                {1,2,3},
                {7,8,9},
                {10,11,12},
        };
        double[] ib2=new double[]{4,5,6};
        double[] ic2=Linalg.inner(ia2,ib2);
        for(int i=0;i<ic2.length;i++){
            System.out.println(ic2[i]+" ");
        }
        System.out.println();

        double[][] a = new double[][]{
                {1, 2, 5},
                {3, 4, 6},
                {3, 4, 6},
                {3, 4, 6},
                {3, 4, 6}
        };

        double[][] b = new double[][]{
                {5, 6, 1, 8},
                {7, 8, 2, 9},
                {9, 10, 2, 10}
        };

        double[][] data=null;
        data= Linalg.dot(a,b);

        for(int i=0;i<data.length;i++){
            for(int j=0;j<data[i].length;j++){
                System.out.println(data[i][j]+" ");
            }
            System.out.println();
        }
    }
}
