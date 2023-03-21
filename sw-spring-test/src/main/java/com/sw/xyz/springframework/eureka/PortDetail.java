package com.sw.xyz.springframework.eureka;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/02/01 9:17
 */
@Data
public class PortDetail {

    @JSONField(name = "$" )
    private long port;


}
