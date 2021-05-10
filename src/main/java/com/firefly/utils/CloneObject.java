package com.firefly.utils;

import com.firefly.layers.core.Model;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拷贝对象
 */
public class CloneObject<T> {
    private Serializable object;
    private byte[] objectByte;
    private ByteArrayInputStream bis;
    private Map<Thread,T> map=new ConcurrentHashMap();

    public CloneObject(Serializable object) throws IOException {
        this.object = object;

        // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。
        // 所以利用这个特性可以实现对象的深拷贝。
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);

        // 将流序列化成对象
        objectByte=bos.toByteArray();

        oos.close();
        bos.close();

        // 将流序列化成对象
        bis = new ByteArrayInputStream(objectByte);
    }

    /**
     * 深度拷贝对象
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public T deepClone() throws IOException, ClassNotFoundException {
//        long start=System.currentTimeMillis();

        // 将流序列化成对象
        ByteArrayInputStream bis = new ByteArrayInputStream(objectByte);
        ObjectInputStream ois = new ObjectInputStream(bis);

        T t=(T)ois.readObject();

        ois.close();
        bis.close();

//        System.out.println("时间:"+(System.currentTimeMillis()-start));

        return t;
    }

    /**
     * 拷贝当前线程对象
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public T currentThreadDeepClone() throws IOException, ClassNotFoundException {
        Thread t= Thread.currentThread();
        T obj=map.get(t);
        if(obj==null){
            obj=deepClone();
            map.put(t,obj);
        }
        return obj;
    }
}
