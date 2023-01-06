package com.sw.xyz.springframework.core.valid;

import com.sw.xyz.springframework.bean.entity.enums.BaseEnums;
import com.sw.xyz.springframework.bean.entity.enums.SystemRespCodeEnums;
import com.sw.xyz.springframework.bean.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/1/5 8:55
 */
@Slf4j
public class EnumValid implements ConstraintValidator<Enums,Object>, Annotation {


    private List<Object> values=new ArrayList<>();

    @Override
    public void initialize(Enums enums) {
        Class<? extends BaseEnums> clazz=enums.clazz();
        Object[] objects=clazz.getEnumConstants();
        try{
            Method method=clazz.getMethod("getCode");
            if (ObjectUtils.isEmpty(method)) {
                throw new BaseException(SystemRespCodeEnums.BAD_REQUEST.getCode(),"缺少[" + clazz.getName() + "]字段");
            }
            for (Object obj : objects) {
                values.add(method.invoke(obj));
            }
        } catch (Exception e) {
            log.error("[处理枚举校验异常]",e);
            throw new BaseException(SystemRespCodeEnums.BAD_REQUEST.getCode(),"入参取值错误");
        }
    }

    @Override
    public boolean isValid(Object value,ConstraintValidatorContext context) {
        return ObjectUtils.isEmpty(value) || values.contains(value);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

}
