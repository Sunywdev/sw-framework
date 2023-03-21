package com.sw.xyz.springframework.eureka;

import lombok.Data;

import java.util.List;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/02/01 9:09
 */
@Data
public class ApplicationDetail {

    private String name;

    private List<EurekaServerDetail> instance;
}
