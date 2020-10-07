package test.mask.data;

import org.jcp.xml.dsig.internal.dom.DOMUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadData {
    public static List<double[]> load(File file) throws IOException {
        List<double[]> list=new ArrayList<double[]>();
        BufferedReader reader=new BufferedReader(new FileReader(file));
        String inStr="";
        while((inStr = reader.readLine())!= null){
            double[] row=strs2Double(inStr.split(","));
            list.add(row);
        }
        return list;
    }

    private static double[] strs2Double(String[] strs){
        double[] ret=new double[strs.length];
        for(int i=0;i<strs.length;i++){
            ret[i]=Double.parseDouble(strs[i]);
        }
        return ret;
    }
}
