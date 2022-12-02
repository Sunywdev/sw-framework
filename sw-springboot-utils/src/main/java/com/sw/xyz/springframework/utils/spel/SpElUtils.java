package com.sw.xyz.springframework.utils.spel;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 名称: SpelUtils
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/12/2 0002 14:59
 */
@Component
@Slf4j
public class SpElUtils {
    /**
     * 用于SpEL表达式解析.
     */
    private final static SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private final static DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private BeanResolver beanResolver;

    public SpElUtils(BeanFactory beanFactory) {
        this.beanResolver = new BeanFactoryResolver(beanFactory);
    }

    public String parseSpEl(Method method, String[] keys, Object[] args) {
        StringBuilder sbu = new StringBuilder();
        try {
            if (!ArrayUtil.isEmpty(keys)) {
                StandardEvaluationContext context = new MethodBasedEvaluationContext(null, method, args, nameDiscoverer);
                context.setBeanResolver(beanResolver);
                for (int i = 0; i < keys.length; i++) {
                    String value = keys[i];
                    if (Validator.isNotEmpty(value)) {
                        String parseValue = parser.parseExpression(value).getValue(context, String.class);
                        sbu.append(parseValue);
                        if (i < keys.length - 1) {
                            sbu.append(".");
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("spEl解析失败,错误信息[{}]", e.getMessage());
        }
        return sbu.toString();
    }
}
