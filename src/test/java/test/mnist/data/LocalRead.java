package test.mnist.data;

import test.image.ImageUtil;

import java.io.File;

public class LocalRead {
    public static double[][] getImages(File[] files){
        double[][] retVal=new double[files.length][];
        for(int i=0;i<files.length;i++){
            int[][] data=ImageUtil.getImagePixel(files[i]);
            retVal[i]= ImageUtil.pixel2double(data);
        }
        return retVal;
    }

    public static double[] getLabels(File[] files){
        double[] retVal=new double[files.length];
        for(int i=0;i<files.length;i++){
            String name=files[i].getName();
            retVal[i]= Double.parseDouble(getFileNameNoEx(name));
        }
        return retVal;
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
}
