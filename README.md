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


## 部署
```shell
$ vi ~/.bash_profile
export JAVA_HOME=/opt/jdk1.8.0_151
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
mvn clean
mvn eclipse:clean eclipse:eclipse -DdownloadSources=true
mvn dependency:sources
mvn dependency:tree
mvn compile
mvn test
mvn package -DskipTests
mvn spring-boot:run
- http://127.0.0.1:8080/
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
docker rm $(docker ps --filter status=exited -q)
docker rmi $(docker images --filter since=johncarnell/spmia-jdk -q)
```

## 准备依赖环境
```shell
$ cd spmia-base/
$ docker build -t johncarnell/spmia-jdk docker/
```


# Examples
=============================

## 第1章 欢迎来到Cloud和Spring
```shell
$ cd spmia-chapter1/
# run jar
$ mvn package -DskipTests
$ java -jar simpleservice/target/*.jar
- http://localhost:8080/hello/john/carnell (Postman/RESTClient)
# run docker
$ mvn docker:build
$ docker-compose –f docker/common/docker-compose.yml up
```

## 第2章 使用Spring Boot构建微服务
```shell
$ cd spmia-chapter2/
$ mvn package -DskipTests
$ java -jar licensing-service/target/*.jar
$
$ mvn docker:build
$ docker-compose –f docker/common/docker-compose.yml up
```

## 第3章 使用Spring Cloud Configuration Server控制配置
```shell
$ cd spmia-base/compose
$ docker-compose up -d postgres
$ docker-compose up -d postgres_dev
```

```shell
$ cd spmia-chapter3/
$ mvn package -DskipTests
$ export ENCRYPT_KEY="IMSYMMETRIC"
$
$ java -jar confsvr/target/*.jar
$ java -jar confsvr/target/*.jar --spring.cloud.config.server.encrypt.enabled=false
$ java -jar confsvr/target/*.jar --spring.profiles.active=gitrepo
$
$ java -jar licensing-service/target/*.jar
$ java -Dspring.cloud.config.uri=http://localhost:8888 \
  -Dspring.profiles.active=dev \
  -jar licensing-service/target/*.jar
$
$ mvn docker:build
$ cd docker/common/
$ cd docker/dev/
$ docker-compose up -d
$ docker-compose logs -f licensingservice
$ docker-compose exec licensingservice ping database
$ docker-compose stop
```

