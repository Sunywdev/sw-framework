package com.sw.xyz.springframework.utils.json;

import java.util.List;
import java.util.Map;

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: fastJson工具类
 * 时间: 2021/12/16 10:52
 */
public class FastJsonUtil {

    /**
     * Object转换json
     *
     * @param object
     * @return
     */
    public static String tojson(Object object) {
        return JSON.toJSONString(object);

    }

    /**
     * json转换clazz
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToobject(String json,Class<T> clazz) {
        return JSON.parseObject(json,clazz);
    }

    /**
     * clazz转换map
     *
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String,Object> objectTomap(Object object) {
        return JSON.parseObject(tojson(object),Map.class);

    }

    /**
     * map转换clazz
     *
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T mapTobean(Map<?,?> map,Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(map),clazz);
    }

    /**
     * Object 拷贝 Object
     *
     * @param obj
     * @param clazz
     * @return
     */
    public static <T> T ObjectCopyObject(Object obj,Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(obj),clazz);
    }

    /**
     * Map转换Json
     *
     * @param mapTxn
     * @return
     */
    public static String mapTOjson(Map<?,?> mapTxn) {
        if (CollectionUtils.isEmpty(mapTxn))
            return null;
        return tojson(mapTxn);
    }

    /**
     * List转换Json
     *
     * @param list
     * @return
     */
    public static String listTOjson(List<?> list) {
        if (CollectionUtils.isEmpty(list))
            return null;
        return tojson(list);
    }

    /**
     * json转换List
     *
     * @param json
     * @return List
     */
    public static <T> T jsonTOlist(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return (T) jsonToobject(json,List.class);
    }

    /**
     * json转换List
     *
     * @param json
     * @return List
     */
    public static <T> List<T> jsonTOlist(String json,Class<T> clazz) {
        if (Validator.isEmpty(json)) {
            return null;
        }
        return JSONObject.parseArray(json,clazz);
    }

    /**
     * json转换Map
     *
     * @param json
     * @return Map
     */
    public static Map<?,?> jsonTOmap(String json) {
        if (Validator.isEmpty(json)) {
            return null;
        }
        return jsonToobject(json,Map.class);
    }

    /**
     * 格式化
     *
     * @param jsonStr
     * @return
     * @author lizhgb
     * @Date 2015-10-14 下午1:17:35
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb=new StringBuilder();
        char last='\0';
        char current='\0';
        int indent=0;
        for (int i=0; i < jsonStr.length(); i++) {
            last=current;
            current=jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb,indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb,indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb,indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     * @author lizhgb
     * @Date 2015-10-14 上午10:38:04
     */
    private static void addIndentBlank(StringBuilder sb,int indent) {
        for (int i=0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
