- 配置Redis集群或者单机或者哨兵模式,安装spring官方进行配置
- 如果使用Redisson在启动类增加@EnableReidsson
```
spring:
  redis:
    cluster:
      max-redirects: 3
      nodes: 192.168.32.134:6800,192.168.32.134:6801,192.168.32.134:6802,192.168.32.134:6803,192.168.32.134:6804,192.168.32.134:6805
    lettuce:
      pool:
        max-active: 16
        max-idle: 16
        max-wait: -1ms
        min-idle: 0
    password: test1234
    timeout: 1000
```