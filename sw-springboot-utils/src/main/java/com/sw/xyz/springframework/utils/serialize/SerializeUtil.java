package com.sw.xyz.springframework.utils.serialize;

import cn.hutool.core.lang.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/4/2 11:53
 */
public class SerializeUtil {

    private static Logger logger=LoggerFactory.getLogger(SerializeUtil.class);


    /**
     * 对象序列化为String
     *
     * @param obj 返回对象
     * @return 序列化后的数据
     */
    public static String serialize(Object obj) {
        if (Validator.isEmpty(obj)) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream=null;
        ObjectOutputStream objectOutputStream=null;
        try{
            byteArrayOutputStream=new ByteArrayOutputStream();
            objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toString(StandardCharsets.ISO_8859_1.name());
        } catch (Exception e) {
            logger.error("序列化数据失败",e);
        } finally {
            try{
                assert objectOutputStream != null;
                objectOutputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                logger.error("序列化数据,资源关闭失败",e);
            }
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param str String字符
     * @return Object 对象
     */
    public static Object serializeToObject(String str) {
        ByteArrayInputStream byteArrayInputStream=null;
        ObjectInputStream objectInputStream=null;
        try{
            byteArrayInputStream=new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
            objectInputStream=new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            logger.error("反序列化数据失败",e);
        } finally {
            try{
                assert objectInputStream != null;
                objectInputStream.close();
                byteArrayInputStream.close();
            } catch (IOException e) {
                logger.error("反序列化数据,资源关闭失败",e);
            }
        }
        return null;
    }
}
