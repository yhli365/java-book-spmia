ISBN:9781617293986 <Spring Microservices in Action>/<Spring微服务实战>


# BOOK
=============================
```

```

## 下载

[Spring Microservices in Action](https://www.manning.com/books/spring-microservices-in-action)

[Source code in GitHub](https://github.com/carnellj/spmia_overview)

[Spring Boot](http://projects.spring.io/spring-boot/)

[Spring Cloud](http://projects.spring.io/spring-cloud/)

[Spring Initializr](http://start.spring.io/)

[Postman](https://www.getpostman.com/)


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
```

## 第2章 
```shell
$ cd spmia-chapter2/
# run jar
$ mvn package -DskipTests
$ java -jar licensing-service/target/*.jar
```
