package com.holly.top.springframework.bean.entity.enums;


/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2021/4/13 9:49
 */
public interface BaseEnums {

    /**
     * 根据 code 获取到相应的枚举类型
     *
     * @param enumClass
     * @param code
     * @param <E>
     * @return
     */
    static <E extends Enum<?> & BaseEnums> E codeOf(Class<E> enumClass,Integer code) {
        if (null != code) {
            for (E e : enumClass.getEnumConstants()) {
                if (code.equals(e.getCode())){
                    return e;
                }
            }
        }
        return null;
    }

    Integer getCode();
}
