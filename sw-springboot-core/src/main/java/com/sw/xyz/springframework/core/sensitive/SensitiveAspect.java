package com.sw.xyz.springframework.core.sensitive;

import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.core.annocation.Sensitive;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author : sunyw
 * @date : 2022-05-24
 * @function:
 **/
@Aspect
@Component
@Order(2)
public class SensitiveAspect {
    @Pointcut("@annotation(com.sw.xyz.springframework.core.annocation.Sensitive)")
    public void pointCut(){

    }

    @AfterReturning(returning = "data",pointcut = "pointCut()")
    public Object around(JoinPoint joinPoint,Object data) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Sensitive annotation = method.getAnnotation(Sensitive.class);
        if (null == annotation) {
            return data;
        }
        if (data instanceof BaseResponse){
            BaseResponse<?> commonResponse =(BaseResponse) data;
            this.replace(annotation,commonResponse.getData());
        }else {
            return data;
        }
        return data;
    }


   private void replace(Sensitive irsSensitive, Object data) throws IllegalAccessException {
       Class<?> aClass = irsSensitive.targetClass();
       if (null==data){
           return;
       }
       Class<?> dataClass = data.getClass();
       if (dataClass== Field.class){
           dataClass =((Field)data).getType();
       }
       if (Collection.class.isAssignableFrom(dataClass)||dataClass.isArray()){
           //对集合的处理
           Collection collection =(Collection) data;
           if (collection.isEmpty()){
               return;
           }
           for (Object next : collection) {
               replace(irsSensitive,next);
           }
       }else{
           if (dataClass !=aClass){
               return;
           }
           Field[] declaredFields = dataClass.getDeclaredFields();
           for (Field field : declaredFields) {
               field.setAccessible(true);
               if (null == field.getType() ||null ==field.get(data)){
                  continue;
               }
               if (Collection.class.isAssignableFrom(field.getType())||field.getType().isArray()){
                   Object dataIn = field.get(data);
                   replace(irsSensitive,dataIn);
               }else{
                   Sensitive sensitive;
                   if (String.class!=field.getType()||(sensitive=field.getAnnotation(Sensitive.class))==null){
                       continue;
                   }
                   field.setAccessible(true);
                   String value =(String)field.get(data);
                   SensitiveTypeEnum typeEnum =sensitive.value();
                   switch (typeEnum){
                       case CHINESE_NAME:
                           value=SensitiveUtils.chineseName(value);
                           break;
                       case ID_CARD:
                           value=SensitiveUtils.idCardNum(value);
                           break;
                       case PHONE_NO:
                           value=SensitiveUtils.phoneNo(value);
                           break;
                       case BANK_CARD:
                           value=SensitiveUtils.bankCard(value);
                           break;
                       case EMAIL:
                           value=SensitiveUtils.email(value);
                           break;
                       default:
                   }
                   field.set(data,value);
               }
           }
       }
   }
}
