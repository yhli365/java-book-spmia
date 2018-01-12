# BOOK
=============================
```
Spring Microservices in Action(201706,ISBN:9781617293986,John Carnell)
Spring微服务实战(#迷途书童译)
```

## 下载

[Spring Microservices in Action](https://www.manning.com/books/spring-microservices-in-action)

[Source code in GitHub](https://github.com/carnellj/spmia_overview)

[Spring Boot](http://projects.spring.io/spring-boot/)

[Spring Cloud](http://projects.spring.io/spring-cloud/)

[Spring Initializr](http://start.spring.io/)

[Postman](https://www.getpostman.com/)

[Docker Hub](https://hub.docker.com/)

[pgAdmin](https://www.pgadmin.org/)

[DBeaver](https://dbeaver.jkiss.org/)


## 部署
```shell
$ vi ~/.bash_profile
export JAVA_HOME=/opt/jdk1.8.0_151
export JAVA_OPTS=-Xmx100m
export M2_HOME=/opt/apache-maven-3.5.2
export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH
$ java -version
java version "1.8.0_151"
$ mvn -version
Apache Maven 3.5.2
```

## MAVEN
```shell
<repository>
	<id>spring-repo</id>
	<name>Spring Repository</name>
	<url>http://repo.spring.io/release</url>
</repository>

mvn eclipse:clean eclipse:eclipse -DdownloadSources=true
mvn clean package -DskipTests
mvn docker:build
```

## 下载Docker镜像(使用阿里镜像加速器)
```shell
$ docker version         
 Version:      17.05.0-ce
$ docker-compose -version
docker-compose version 1.18.0, build 8dd22a9
```

```shell
docker pull openjdk:8u151-jdk-alpine
docker pull postgres:9.6.6-alpine
docker pull mysql:5.6.37
docker rm $(docker ps --filter status=exited -q)
docker rmi $(docker images --filter since=johncarnell/spmia-jdk -q)
```

## 准备依赖环境
```shell
$ cd spmia-base/
$ docker build -t johncarnell/spmia-jdk docker/
$
# Profile为default时使用postgres数据库，通过docker运行。
# Profile为dev时使用mysql数据库，通过手动执行java进程运行。
$ cd spmia-base/compose
$ docker-compose up -d postgres
$ docker-compose up -d mysql
$
$ vi /etc/hosts
10.10.8.11 database
```

```mysql
CREATE DATABASE eagle_eye_dev DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
```


# Examples
=============================

## 第1章 欢迎来到Cloud和Spring
```shell
$ cd spmia-chapter1/
$ mvn clean package -DskipTests
$
$ java $JAVA_OPTS -jar simpleservice/target/*.jar
- http://localhost:8080/hello/john/carnell (Postman/RESTClient)
```

```shell
$ mvn docker:build
$ cd docker/common/
$ docker-compose up
```

## 第2章 使用Spring Boot构建微服务
```shell
$ cd spmia-chapter2/
$ mvn clean package -DskipTests
$
$ java $JAVA_OPTS -jar licensing-service/target/*.jar
```

```shell
$ mvn docker:build
$ cd docker/common/
$ docker-compose up
```

## 第3章 使用Spring Cloud Configuration Server控制配置
```shell
$ cd spmia-chapter3/
$ mvn clean package -DskipTests
$
$ export ENCRYPT_KEY="IMSYMMETRIC"
$ java $JAVA_OPTS -jar confsvr/target/*.jar
$ java $JAVA_OPTS -jar confsvr/target/*.jar --spring.cloud.config.server.encrypt.enabled=false
$ java $JAVA_OPTS -jar confsvr/target/*.jar --spring.profiles.active=gitrepo
$
$ java $JAVA_OPTS -jar licensing-service/target/*.jar
$
$ java -Dspring.cloud.config.uri=http://localhost:8888 \
  -Dspring.profiles.active=dev \
  -jar licensing-service/target/*.jar
```

```shell
$ mvn docker:build
$ cd docker/common/
$ cd docker/dev/
$ docker-compose up -d
$ docker-compose logs -f licensingservice
$ docker-compose exec licensingservice ping database
$ docker-compose stop
```

## 第4章 服务发现
```shell
$ cd spmia-chapter4/
$ mvn clean package -DskipTests
$
$ java $JAVA_OPTS -jar confsvr/target/*.jar
$ java $JAVA_OPTS -jar eurekasvr/target/*.jar
$ java $JAVA_OPTS -jar organization-service/target/*.jar --server.port=8085
$ java $JAVA_OPTS -jar organization-service/target/*.jar --server.port=8086
$ java $JAVA_OPTS -jar licensing-service/target/*.jar --server.port=8080
```

```shell
$ mvn docker:build
$ cd docker/common/
$ docker-compose up -d
$ docker-compose logs -f licensingservice
```

## 第5章 坏事发生时：使用Spring Cloud和Netflix Hystrix的客户端弹性模式
```shell
$ cd spmia-chapter5/
$ mvn clean package -DskipTests
$
$ java $JAVA_OPTS -jar eurekasvr/target/*.jar
$ java $JAVA_OPTS -jar confsvr/target/*.jar
$ java $JAVA_OPTS -jar organization-service/target/*.jar --server.port=8085 --spring.profiles.active=dev
$ java $JAVA_OPTS -jar licensing-service/target/*.jar --server.port=8080 --spring.profiles.active=dev
```

```shell
$ mvn docker:build
$ cd docker/common/
$ docker-compose up -d
$ docker-compose logs -f licensingservice
```

## 第6章 使用Spring Cloud和Zuul进行服务路由
```shell
$ cd spmia-chapter6/
$ mvn clean package -DskipTests
$
$ vi /etc/hosts
127.0.0.1 orgservice-new
$
$ java $JAVA_OPTS -jar eurekasvr/target/*.jar
$ java $JAVA_OPTS -jar confsvr/target/*.jar
$ java $JAVA_OPTS -jar zuulsvr/target/*.jar --management.security.enabled=false --spring.profiles.active=dev
$ java $JAVA_OPTS -jar organization-service/target/*.jar --server.port=8085 --spring.profiles.active=dev
$ java $JAVA_OPTS -jar orgservice-new/target/*.jar --server.port=8087 --spring.profiles.active=dev
$ java $JAVA_OPTS -jar specialroutes-service/target/*.jar --server.port=8910 --spring.profiles.active=dev
$ java $JAVA_OPTS -jar licensing-service/target/*.jar --server.port=8080 --spring.profiles.active=dev
```

```shell
$ mvn docker:build
$ cd docker/common/
$ docker-compose up -d
```
