package com.sw.xyz.springframework.cache.utils;

import com.sw.xyz.springframework.bean.entity.enums.RespCodeEnums;
import com.sw.xyz.springframework.bean.exceptions.BaseException;
import com.sw.xyz.springframework.utils.spring.SpringContextUtils;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 名称 :  redis操作工具类
 * 功能 :  <功能详细描述>
 * 方法 :  <方法简介-方法描述>
 * 版本 :  1.0
 * 日期 :  2021/4/14 16:14
 * 作者 :  sunyw
 * 说明 :  redis操作工具类
 */
public class RedisUtils<T> {

    private static Logger log=LoggerFactory.getLogger(RedisUtils.class);

    /**
     * RedisTemplate对象,从spring上下文中获取
     */

    private static final RedisTemplate<String,Object> redisTemplate= SpringContextUtils.getInstance().getBean("redisTemplate",RedisTemplate.class);


    /**
     * 从List中拿到一条数据,从最左边,但是不删除
     *
     * @param key list key
     * @return String
     */
    public static Object rangeFromList(String key) {
        List<Object> range=redisTemplate.opsForList().range(key,0,0);
        if (!CollectionUtils.isEmpty(range)) {
            return range.get(0);
        }
        return null;
    }

    /**
     * 从List中拿数据,指定开始索引和结束索引
     *
     * @param key        list的key
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @return List<Object> 返回指定区间的list值
     */
    public static List<Object> rangeFromList(String key,long startIndex,long endIndex) {
        return redisTemplate.opsForList().range(key,startIndex,endIndex);
    }

    /**
     * 往集合最左边添加指定的数据
     *
     * @param key   list的key
     * @param value 添加的数据
     */
    public static void leftPush(String key,Object value) {
        redisTemplate.opsForList().leftPush(key,value);
    }

    /**
     * 从List中拿到一条数据,从最左边,弹出数据并删除
     *
     * @param key list的key
     * @return Object 最左边的数据
     */
    public static Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }


    /**
     * 存储hash的数据结构
     *
     * @param key   key值
     * @param value value 值
     * @param time  时间
     * @param unit  时间单位
     * @return 存储结果
     */
    public static boolean saveValue(String key,Object value,long time,TimeUnit unit) {
        if (StringUtil.isNullOrEmpty(key)) {
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"key不能为空!");
        }
        if (ObjectUtils.isEmpty(value)) {
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"value不能为空!");
        }
        try{
            redisTemplate.opsForValue().set(key,value,time,unit);
        } catch (Exception e) {
            log.error("redis 执行saveHash错误,错误详情:",e);
            return false;
        }
        log.debug("redis 执行saveHash成功");
        return true;
    }

    /**
     * 存储hash的数据结构
     *
     * @param key   key值
     * @param value value 值
     * @return 存储结果
     */
    public static boolean saveValue(String key,Object value) {
        if (StringUtil.isNullOrEmpty(key)) {
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"key不能为空!");
        }
        if (ObjectUtils.isEmpty(value)) {
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"value不能为空!");
        }
        try{
            redisTemplate.opsForValue().set(key,value);
        } catch (Exception e) {
            log.error("redis 执行saveHash错误,错误详情:",e);
            return false;
        }
        log.debug("redis 执行saveHash成功");
        return true;
    }

    /**
     * 获取hash value
     *
     * @param key key
     * @return value
     */
    public static Object getHashValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 插入集合
     *
     * @param key  key
     * @param args value值
     * @param time 过期时间
     * @param unit 时间类型
     * @return long 现有长度
     */
    public static Long saveAllList(String key,long time,TimeUnit unit,Object... args) {
        Long push;
        try{
            push=redisTemplate.opsForList().leftPushAll(key,args);
            redisTemplate.expire(key,time,unit);
        } catch (Exception e) {
            log.error("redis 执行saveAllList错误,错误详情:",e);
            push=0L;
        }
        log.debug("redis 执行saveAllList成功,现有size为:{}",push);
        return push;
    }

    /**
     * 插入集合
     *
     * @param key  key
     * @param args value值
     * @return long 现有长度
     */
    public static Long saveAllList(String key,Object... args) {
        Long push;
        try{
            push=redisTemplate.opsForList().leftPushAll(key,args);
        } catch (Exception e) {
            log.error("redis 执行saveAllList错误,错误详情:",e);
            push=0L;
        }
        log.debug("redis 执行saveAllList成功,现有size为:{}",push);
        return push;
    }

    /**
     * 往list中插入单条数据
     *
     * @param key  key
     * @param arg  value值
     * @param time 过期时间
     * @param unit 时间类型
     * @return long 现有长度
     */
    public static Long saveList(String key,long time,TimeUnit unit,Object arg) {
        Long push;
        try{
            push=redisTemplate.opsForList().leftPush(key,arg);
            redisTemplate.expire(key,time,unit);
        } catch (Exception e) {
            log.error("redis 执行saveList错误,错误详情:",e);
            push=0L;
        }
        log.debug("redis 执行saveList成功,现有size为:{}",push);
        return push;
    }

    /**
     * 往list中插入单条数据
     *
     * @param key key
     * @param arg value
     * @return long 现有长度
     */
    public static Long saveList(String key,Object arg) {
        Long push;
        try{
            push=redisTemplate.opsForList().leftPush(key,arg);
        } catch (Exception e) {
            log.error("redis 执行saveList错误,错误详情:",e);
            push=0L;
        }
        log.debug("redis 执行saveList成功,现有size为:{}",push);
        return push;
    }

    /**
     * 查询key对应的list的全部数据信息
     *
     * @param key key值
     * @return list 全部数据
     */
    public List<T> queryList(String key) {
        return (List<T>) redisTemplate.boundListOps(key).range(0,-1);
    }

    /**
     * 查询 list数组下的指定位置的数据
     *
     * @param key   key值
     * @param start 开始角标从0开始
     * @param end   结束角标
     * @return list 指定位置的数据
     */
    public static List<Object> queryList(String key,int start,int end) {
        return redisTemplate.boundListOps(key).range(start,end);
    }

    /**
     * save
     *
     * @param key  key
     * @param data map
     * @param time 过期时间
     * @param unit 时间格式
     * @param <T>  <></>
     */
    public static <T> void saveMap(String key,Map<String,T> data,long time,TimeUnit unit) {
        try{
            //判断当前key是否已经存在
            Boolean aBoolean=redisTemplate.hasKey(key);
            if (!ObjectUtils.isEmpty(aBoolean) && aBoolean) {
                //已经存在使用追加添加
                log.info("redis 执行add完毕");
                addMap(key,data);
            } else {
                //不存在就新增一个,设置过期时间为一天
                redisTemplate.opsForHash().putAll(key,data);
                Boolean expire=redisTemplate.expire(key,time,unit);
                if (!ObjectUtils.isEmpty(expire) && expire) {
                    log.info("redis 执行save完毕");
                } else {
                    //先刪除再抛异常
                    deleteByKey(key);
                    log.info("redis 执行delete完毕");
                    throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"设置过期时间失败,数据已做清除");
                }
            }
        } catch (BaseException e) {
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error("redis进行save时错误,错误信息",e);
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"执行save时错误:" + e.getMessage());
        }
    }

    /**
     * save
     *
     * @param key  key
     * @param data map
     * @param <T>  <></>
     */
    public static <T> void saveMap(String key,Map<String,T> data) {
        try{
            //判断当前key是否已经存在
            Boolean aBoolean=redisTemplate.hasKey(key);
            if (!ObjectUtils.isEmpty(aBoolean) && aBoolean) {
                //已经存在使用追加添加
                log.info("redis 执行add完毕");
                addMap(key,data);
            } else {
                //不存在就新增一个,设置过期时间为一天
                redisTemplate.opsForHash().putAll(key,data);
            }
        } catch (Exception e) {
            log.error("redis进行save时错误,错误信息",e);
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"执行save时错误:" + e.getMessage());
        }
    }

    /**
     * 为指定的key对应的map中添加数据
     *
     * @param key   key
     * @param filed 往key对应map中添加的字段
     * @param value 往key对应map中添加的值
     */
    public static <T> void addFiled(String key,String filed,T value) {
        try{
            redisTemplate.opsForHash().put(key,filed,value);
        } catch (Exception e) {
            log.error("redis进行saveFiledToMap时错误,错误信息",e);
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"执行addFiled时错误:" + e.getMessage());
        }
    }

    /**
     * key对应的map中添加缓存对象
     *
     * @param key 键值
     * @param map map集合
     */
    private static <T> void addMap(String key,Map<String,T> map) {
        try{
            redisTemplate.opsForHash().putAll(key,map);
        } catch (Exception e) {
            log.error("redis进行addToMap时错误,错误信息",e);
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"执行addMap时错误:" + e.getMessage());
        }
    }

    /**
     * 获取key对应的map集合
     *
     * @param key 键值
     * @return Map<Object,Object> 键值下对应的Map
     */
    public static Map<Object,Object> queryMapByKey(String key) {
        return redisTemplate.boundHashOps(key).entries();
    }

    /**
     * 获取key对应map中的值
     *
     * @param key   键值
     * @param Filed map中的键值
     * @param clazz 获取对象转换
     * @return T
     */
    public static <T> T queryMapFieldByKey(String key,String Filed,Class<T> clazz) {
        return (T) redisTemplate.boundHashOps(key).get(Filed);
    }

    /**
     * 删除
     *
     * @param key 键值
     */
    public static void deleteByKey(String key) {
        try{
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("redis进行delete时错误,错误信息",e);
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"执行deleteByKey时错误:" + e.getMessage());
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键值
     * @return boolean
     */
    public static boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }


    /**
     * 获取自增id一天重置一次
     *
     * @param key key
     * @return String value
     */
    public static String getAutoId(String key) {
        Long increment=redisTemplate.opsForValue().increment(key);
        if (ObjectUtils.isEmpty(increment)) {
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"生成自增序列错误!");
        }
        if (1 == increment) {
            redisTemplate.expire(key,1,TimeUnit.DAYS);
        }
        return String.valueOf(increment);
    }

    /**
     * redis list结构分页查询
     *
     * @param listKey  listKey
     * @param pageNo   当前页
     * @param pageSize 条数
     * @param clazz    返回类泛型
     * @return List<T>
     */
    public List<T> pageInfo(String listKey,int pageNo,int pageSize,Class<T> clazz) {
        if (pageNo <= 0) {
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"页数需要大于0");
        }
        if (pageSize <= 0) {
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"条数需要大于0");
        }
        int start=pageSize * (pageNo - 1); // 因为redis中list元素位置基数是0
        int end=start + pageSize - 1;
        return (List<T>) redisTemplate.opsForList().range(listKey,start,end);
    }

    /**
     * 长度
     *
     * @param key
     * @param obj
     * @return
     */
    public static Long size(String key,Class obj) {
        try{
            if (obj.isInterface()) {
                if (obj.equals(List.class)) {
                    return redisTemplate.opsForList().size(key);
                } else if (obj.equals(Map.class)) {
                    return redisTemplate.opsForHash().size(key);
                } else if (obj.equals(String.class)) {
                    return redisTemplate.opsForValue().size(key);
                } else if (obj.equals(Set.class)) {
                    Long size=redisTemplate.opsForSet().size(key);
                    if (ObjectUtils.isEmpty(size)) {
                        size=redisTemplate.opsForZSet().size(key);
                    }
                    return size;
                }
            } else {
                if (obj.newInstance() instanceof List) {
                    return redisTemplate.opsForList().size(key);
                } else if (obj.newInstance() instanceof Map) {
                    return redisTemplate.opsForHash().size(key);
                } else if (obj.newInstance() instanceof String) {
                    return redisTemplate.opsForValue().size(key);
                } else if (obj.newInstance() instanceof Set) {
                    Long size=redisTemplate.opsForSet().size(key);
                    if (ObjectUtils.isEmpty(size)) {
                        size=redisTemplate.opsForZSet().size(key);
                    }
                    return size;
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("计算Redis key:{}出错,错误信息",key,e);
        }
        return null;
    }

    /**
     * 匹配获取所有键下的值
     *
     * @param pattern pattern
     * @return List<T>
     */
    public List<T> keysValues(String pattern) {
        Set<String> keys=redisTemplate.keys(pattern);
        List<T> list=new ArrayList<>();
        if (!CollectionUtils.isEmpty(keys)) {
            for (String key : keys) {
                List<T> info=this.queryList(key);
                list.addAll(info);
            }
        }
        return list;
    }

    /**
     * 匹配获取所有的键
     *
     * @param pattern pattern
     * @return Set<String>
     */
    public static Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

}
