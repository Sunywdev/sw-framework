package com.sw.xyz.springframework.core.annocation;

import com.sw.xyz.springframework.bean.entity.enums.BaseEnums;
import com.sw.xyz.springframework.core.valid.EnumValid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/1/5 8:52
 */
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= EnumValid.class)
@Documented
public @interface Enums {

    /**
     * 验证的枚举类class
     */
    Class<? extends BaseEnums> clazz();

    /**
     * 错误描述
     */
    String message() default "请求参数不在允许范围之内";


    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};
}
