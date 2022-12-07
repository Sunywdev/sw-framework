package com.sw.xyz.springframework.core.sensitive;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Sensitive {

    /*
     * 指定字段 默认为NONE
     */
    SensitiveTypeEnum value() default SensitiveTypeEnum.NONE;

    /*
     * 返回的class类
     */
    Class <?> targetClass() default Void.class;
}
