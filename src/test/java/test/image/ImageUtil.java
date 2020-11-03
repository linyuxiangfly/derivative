package test.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    public enum RGB{
        red,
        green,
        blue,
        rgb
    }
    public static BufferedImage loadImage(File file){
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bi;
    }
    /**
     * 读取一张图片的RGB值
     *
     * @throws Exception
     */
    public static int[][] getImagePixel(File file) {
        BufferedImage bi = loadImage(file);
        int width = bi.getWidth();
        int height = bi.getHeight();
        int[][] retVal=new int[height][width];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                retVal[j][i]=bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
            }
        }
        return retVal;
    }

    public static double[][][] pixel2double3d(int[][] data){
        int widthSrc=data[0].length;
        int heightSrc=data.length;
        double[][][] retVal=new double[heightSrc][widthSrc][1];
        long val=0;

        int x;
        int y;

        for(y=0;y<heightSrc;y++){
            for(x=0;x<widthSrc;x++){
                val=(data[y][x] & 0xffffff);
                double v=((val >> 16)+(val >> 8)+(val & 0xff))/3.0/255.0;
                retVal[y][x][0]=v;
            }
        }
        return retVal;
    }

    public static double[] pixel2double(int[][] data){
        int widthSrc=data[0].length;
        int heightSrc=data.length;
        double[] retVal=new double[heightSrc*widthSrc];
        long val=0;

        int x;
        int y;

        for(y=0;y<heightSrc;y++){
            for(x=0;x<widthSrc;x++){
                val=(data[y][x] & 0xffffff);
                double v=((val >> 16)+((val & 0xff00) >> 8)+(val & 0xff))/3.0/255.0;
//                if(v!=0){
//                    System.out.println();
//                }
                retVal[y*widthSrc+x]=v;
            }
        }
        return retVal;
    }

    /**
     * 压缩数据
     * @param data
     * @param width
     * @param height
     * @return
     */
    public static int[][] compressDataRGB(int[][] data, int width, int height, RGB rgb){
        int[][] retVal=new int[height][width];
        int widthSrc=data[0].length;
        int heightSrc=data.length;
        long val=0;

        //计算缩放比例
        float sx=width/(float)widthSrc;
        float sy=height/(float)heightSrc;
        int x;
        int y;

        for(y=0;y<heightSrc;y++){
            for(x=0;x<widthSrc;x++){
                val=(data[y][x] & 0xffffff);

                val=(val >> 16);
                val=((val & 0xff00) >> 8);
                val=(val & 0xff);
                switch (rgb){
                    case red:
                        val=(val >> 16);
                        break;
                    case green:
                        val=((val & 0xff00) >> 8);
                        break;
                    case blue:
                        val=(val & 0xff);
                        break;
                    case rgb:
                        val=data[y][x];
                        break;
                }
                retVal[(int)(sy*y)][(int)(sx*x)]=(int)val;
            }
        }
        return retVal;
    }

    public static void writeImage(File file, String type, int[][] rgbArray){
        // 获取数组宽度和高度
        int width = rgbArray[0].length;
        int height = rgbArray.length;
        // 将二维数组转换为一维数组
        int[] data = new int[width*height];
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                data[i*width + j] = rgbArray[i][j];
        // 将数据写入BufferedImage
        BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        bf.setRGB(0, 0, width, height, data, 0, width);
        // 输出图片
        try {
            ImageIO.write((RenderedImage)bf, type, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
