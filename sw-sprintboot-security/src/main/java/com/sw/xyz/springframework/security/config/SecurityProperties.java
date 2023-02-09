package com.sw.xyz.springframework.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * 名称: SecurityProperties
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/12/8 0008 15:05
 */

@Configuration
@ConfigurationProperties(
        prefix = "sw.security"
)
@Data
public class SecurityProperties implements Serializable {

    /**
     * 忽略鉴权路径,多个以,分割,已经配置过了swagger的过滤配置
     */
    private String ignorePath;

    /**
     * 开启权限过滤器,默认开启,为false时则不再拦截请求校验,推荐在开发调试时使用,生产和测试环境禁止使用
     */
    private Boolean enableSecurityFilter =true;

    /**
     * token名称
     */
    private String tokenName = "sw";

    /**
     * token的长久有效期(单位:秒) 默认30天, -1代表永久
     */
    private long timeout = 60 * 60 * 24 * 30;

    /**
     * token生成样式
     */
    private String tokenStyle = "random-128";

    /**
     * token临时有效期 [指定时间内无操作就视为token过期] (单位: 秒), 默认-1 代表不限制
     * (例如可以设置为1800代表30分钟内无操作就过期)
     */
    private long activityTimeout = 1800;

    /**
     * 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
     */
    private Boolean isConcurrent = true;

    /**
     * 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
     */
    private Boolean isShare = true;

    /**
     * 同一账号最大登录数量，-1代表不限 （只有在 isConcurrent=true, isShare=false 时此配置才有效）
     */
    private int maxLoginCount = 12;

    /**
     * 是否尝试从请求体里读取token
     */
    private Boolean isReadBody = true;

    /**
     * 是否尝试从header里读取token
     */
    private Boolean isReadHeader = true;

    /**
     * 是否尝试从cookie里读取token
     */
    private Boolean isReadCookie = true;

    /**
     * 是否在登录后将 Token 写入到响应头
     */
    private Boolean isWriteHeader = false;

    /**
     * 默认dao层实现类中，每次清理过期数据间隔的时间 (单位: 秒) ，默认值30秒，设置为-1代表不启动定时清理
     */
    private int dataRefreshPeriod = 30;

    /**
     * 获取[token专属session]时是否必须登录 (如果配置为true，会在每次获取[token-session]时校验是否登录)
     */
    private Boolean tokenSessionCheckLogin = true;

    /**
     * 是否打开自动续签 (如果此值为true, 框架会在每次直接或间接调用getLoginId()时进行一次过期检查与续签操作)
     */
    private Boolean autoRenew = true;

    /**
     * token前缀, 格式样例(satoken: Bearer xxxx-xxxx-xxxx-xxxx)
     */
    private String tokenPrefix;

    /**
     * 是否在初始化配置时打印版本字符画
     */
    private Boolean isPrint = false;

    /**
     * 是否打印操作日志
     */
    private Boolean isLog = false;

    /**
     * 日志等级（trace、debug、info、warn、error、fatal）
     */
    private String logLevel = "trace";

}
