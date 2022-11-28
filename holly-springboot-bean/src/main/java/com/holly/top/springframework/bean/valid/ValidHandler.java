package com.holly.top.springframework.bean.valid;

import com.holly.top.springframework.bean.exceptions.BaseException;
import org.springframework.core.Ordered;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/29 15:26
 */
public interface ValidHandler extends Ordered {

    boolean needValid();

    void valid(Object o) throws BaseException;
}
