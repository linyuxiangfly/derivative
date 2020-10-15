package com.firefly.utils;

import com.firefly.layers.core.Model;

import java.io.*;

/**
 * 模型工具
 */
public class ModelUtil {
    /**
     * 将mode导出文件
     * @param model 模型
     * @param file 文件
     * @throws IOException
     */
    public static void exportModel(Model model, String file) throws IOException {
        FileOutputStream fileOut =
                new FileOutputStream(new File(file));
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(model);
        out.close();
        fileOut.close();
    }

    /**
     * 导入模型
     * @param file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Model importModel(String file) throws IOException, ClassNotFoundException {
        //新建一个模型，将之前模型的参数导入再进行拟合
        Model newModel=null;

        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        newModel = (Model) in.readObject();
        in.close();
        fileIn.close();

        return newModel;
    }
}
