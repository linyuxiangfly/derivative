package test.data;

import com.firefly.layers.data.MultiDim;
import com.firefly.layers.data.Shape;
import com.firefly.layers.data.ShapeIndex;

public class ShapeIndexTest {
    public static void main(String[] args){
        testMultiDim();
        testDouble();
    }

    public static void testMultiDim(){
        MultiDim multiDim=new MultiDim(new Shape(new int[]{2,3,4}));
        ShapeIndex index=new ShapeIndex(multiDim.getShape());

        double[][][] vals=(double[][][])multiDim.getData();
        int[] indexVal=index.getDimIndex();
        long start=System.currentTimeMillis();
        for(int ii=0;ii<1000000;ii++){
            int i=0;
            index.zero();
            do {
                i++;
                multiDim.setVal(index,i);
//                vals[indexVal[0]][indexVal[1]][indexVal[2]]=i;
            }while(index.next());
        }
        long end=System.currentTimeMillis();
        System.out.println("testMultiDim time(ms):"+((end-start)/1000.0));

        start=System.currentTimeMillis();
        for(int ii=0;ii<1000000;ii++){
            int i=0;
            for(int x=0;x<vals.length;x++){
                for(int y=0;y<vals[x].length;y++){
                    for(int z=0;z<vals[x][y].length;z++){
                        i++;
                        vals[x][y][z]=i;
                    }
                }
            }
        }
        end=System.currentTimeMillis();
        System.out.println("testMultiDim2 time(ms):"+((end-start)/1000.0));

        index.last();
        while(true){
            double val=(double)multiDim.getVal(index);
            System.out.println(val);
            if(!index.back()){
                break;
            }
        }
    }

    public static void testDouble(){
        double[][][] multiDim=new double[2][3][4];

        long start=System.currentTimeMillis();
        for(int ii=0;ii<1000000;ii++){
            int i=0;
            for(int x=0;x<multiDim.length;x++){
                for(int y=0;y<multiDim[x].length;y++){
                    for(int z=0;z<multiDim[x][y].length;z++){
                        i++;
                        multiDim[x][y][z]=i;
                    }
                }
            }
        }
        long end=System.currentTimeMillis();
        System.out.println("testDouble time(ms):"+((end-start)/1000.0));

        for(int x=0;x<multiDim.length;x++){
            for(int y=0;y<multiDim[x].length;y++){
                for(int z=0;z<multiDim[x][y].length;z++){
                    System.out.println(multiDim[x][y][z]);
                }
            }
        }
    }
}
