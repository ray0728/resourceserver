![travis_ci](https://www.travis-ci.org/ray0728/resourceserver.svg?branch=master)
# resourceserver
## 说明
提供UGC资源存储管理服务。
* 需配合KAFKA使用
* 提供四个Topic供其他服务使用
  * HLS  
    由[GatewayServer][1]服务消费，指明视频文件的HLS分片任务是否完成
  * NEWS  
    由[GatewayServer][1]服务消费，指明有新的广播消息需要显示在页面中
  * SMS  
    *暂无消费者*
  * FEEDBACK  
    由[DashboardServer][2]服务消费，仅**运维**使用

## ToDo
* 未对做安全授权验证

## 运行方式：  
application.properties中并**不包含**完整配置信息，所以**不支持**直接运行  
* java 方式

```java
java
-Djava.security.egd=file:/dev/./urandom                  \
-Dspring.cloud.config.uri=$CONFIGSERVER_URI              \
-Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI \
-Dspring.zipkin.base-url=$ZIPKIN_URI                     \
-Dspring.redis.host=$REDIS_URI                           \
-Dauth-server=$AUTH_URI                                  \
-Dspring.datasource.url=$DATABASE_URI                    \
-Dresource.upload.dir.root=$UPLOAD_DIR_ROOT              \
-jar target.jar
```
* docker 方式  
建议用docker-compose方式运行

```docker
resourceserver:
#    image: ray0728/resourceserv:1.0
#    ports:
#      - "10005:10003"
#    environment:
#      DATABASE_PORT: "3306"
#      REDIS_PORT: "6379"
#      ZIPKIN_PORT: "9411"
#      CONFIGSERVER_PORT: "10002"
#      EUREKASERVER_PORT: "10001"
#      ACCOUNT_PORT: "10003"
#      RESOURCE_PORT: "10005"
#      CONFIGSERVER_URI:
#      EUREKASERVER_URI:
#      KAFKA_URI:
#      ZIPKIN_URI:
#      REDIS_URI:
#      DATABASE_URI: 数据库地址
#      UPLOAD_DIR_ROOT: "/mnt/resource"
#    volumes:
#      /home/core/resource:/mnt/resource
```  
关于Docker  
编译完成的Docker位于[Dockerhub][3]请结合Release中的[Tag标签][4]获取对应的Docker

[1]:https://github.com/ray0728/gatewayserver
[2]:https://github.com/ray0728/dashboardserver
[3]:https://hub.docker.com/r/ray0728/resourceserv/tags
[4]:https://github.com/ray0728/resourceserver/tags
