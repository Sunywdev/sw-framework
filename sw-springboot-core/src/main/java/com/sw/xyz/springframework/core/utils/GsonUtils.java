package com.sw.xyz.springframework.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * 名称: GsonUtilss
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 17:32
 */
public class GsonUtils {
    private static Gson filterNullGson;
    private static Gson nullableGson;

    static {
        nullableGson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                .create();
        filterNullGson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                .create();
    }

    protected GsonUtils() {
    }

    /**
     * 根据对象返回json   不过滤空值字段
     */
    public static String toJsonWtihNullField(Object obj) {
        return nullableGson.toJson(obj);
    }

    /**
     * 根据对象返回json  过滤空值字段
     */
    public static String toJsonFilterNullField(Object obj) {
        return filterNullGson.toJson(obj);
    }

    /**
     * 将json转化为对应的实体对象
     * new TypeToken<HashMap<String, Object>>(){}.getType()
     */
    public static <T> T fromJson(String json, Type type) {
        return nullableGson.fromJson(json, type);
    }

    /**
     * 将对象值赋值给目标对象
     *
     * @param source 源对象
     * @param <T>    目标对象类型
     * @return 目标对象实例
     */
    public static <T> T convert(Object source, Class<T> clz) {
        String json = GsonUtils.toJsonFilterNullField(source);
        return GsonUtils.fromJson(json, clz);
    }
}
