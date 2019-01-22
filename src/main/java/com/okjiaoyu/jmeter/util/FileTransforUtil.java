package com.okjiaoyu.jmeter.util;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * @Author: liuzhanhui
 * @Decription: 文件转换格式
 * @Date: Created in 2019-01-22:09:49
 * Modify date: 2019-01-22:09:49
 */
public class FileTransforUtil {

    private static final Logger log = Logger.getLogger(FileTransforUtil.class);

    /**
     * 对象转为二进制数组
     * @param object
     * @return
     */
    public static byte[] fileTansforToByte(Object object) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream out = null;
        try {
            baos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos.toByteArray();
    }

    /**
     * 二进制数组转为对象
     * @param bytes
     * @return
     */
    public static Object byteTansforToObject(byte[] bytes){
        Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream in = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            in = new ObjectInputStream(bais);
            obj = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
