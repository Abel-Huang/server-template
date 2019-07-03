# server-template
[![@Abel-Huang](https://img.shields.io/sonar/http/sonar.petalslink.com/org.ow2.petals%3Apetals-se-ase/coverage.svg)](https://github.com/Abel-Huang/micro-server)
[![@Abel-Huang](https://img.shields.io/packagist/l/doctrine/orm.svg)](https://github.com/Abel-Huang/micro-server)
[![@Abel-Huang](https://img.shields.io/uptimerobot/status/m778918918-3e92c097147760ee39d02d36.svg)](https://github.com/Abel-Huang/micro-server)

一个Java服务端项目，主要用于技术的学习和实践，整合Java开源框架。 
## 简介
目前v1版本还是垂直架构，由于已经完成分布式Session，可以进行Tomcat集群部署，缓存也比较容易更改为集群模式。
## 使用的框架和技术
* 项目整体架构：Spring/SpringBoot
* 数据库：MySQL
* 连接池：Druid
* ORM框架：MyBatis
* Web：SpringMVC
* 数据库表分页：PageHelper
* 缓存：Redis
* Redis客户端：Jedis
* 全文搜索引擎：ElasticSearch
* 文件存储：Mongodb
* Servlet容器:Tomcat
* 序列化和反序列化：Jackson
* 日志：log4j和Slfj(使用lombok提供的快捷接口)
* 消息队列：ActiveMQ
* Test：Junit, JMeter, Rest Client/PostMan
* 其他：Guava, lombok等


## 说明
### 接口风格
参数较少的接口已经更改为RestFul风格。

### 接口规范
```json
{
  "meta":{
    "code": 0,
    "message": "success"
  },
  "body":{
  "id": 2,
  "categoryId": 1,
  "name": "Java Core",
  "subTitle": "Famous Java beginner book",
  ...
  },
  "success": true
}  
```

### 分布式Session
考虑到Tomcat集群式部署，将原本由Tomcat管理的Session交由Redis进行管理，实现分布式Session。

### 缓存
采用Redis作为缓存服务器。Redis计划采用一主两从的集群，并且使用3个Sentinel实现高可用。

### 文件服务器
使用MongoDB存储文件，接下来打算换成HBase实现对象存储。

### 全文搜索引擎
完成ActiveMQ和ElasticSearch的整合，实现异步搜索，并且打算将ES API更换为high-level-rest-api。 