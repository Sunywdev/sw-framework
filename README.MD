![avatar](/file/sw-framework.jpg)
# sw-framework 是一个JAVA开发基础框架,目的是为了缩短开发周期,简化开发过程中重复工作
## 模块功能划分
- sw-spring-test         测试工程
- sw-springboot-bean     公共entity(请求响应实体)
- sw-springboot-cache    本地缓存,redis缓存工具,限流,分布式锁,幂等工具
- sw-springboot-config   配置相关(暂未规划)
- sw-springboot-core     核心包,包含公共的工具类,日志工具,健康检查,脱敏,校验等
- sw-springboot-jdbc     数据库相关(后续功能会支持多数据库多库多表)
- sw-springboot-job      定时任务相关
- sw-springboot-mq       消息队列功能封装
- sw-springboot-starter  springboot启动依赖
- sw-springboot-swagger  API接口文档配置
- sw-springboot-utils    工具类
- sw-springcloud-starter springcloud启动依赖
- sw-sprintboot-security 安全模块,以sa-token为基础进行二次封装
