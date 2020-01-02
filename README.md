# dubbo-zookeeper
springboot2.2.2.RELEASE整合dubbo+zookeeper(基于此时都是较新版本)

时间:2020年1月2日

## 准备

在linux上使用docker安装zookeeper(略,自行百度)

**可选项**

安装 可视化监控管理后台 dubbo-admin (不装不影响使用)

1.下载dubbo-admin

地址 ：https://github.com/apache/dubbo-admin/tree/master

2、使用idea open项目

修改 dubbo-admin\src\main\resources \application.properties 指定zookeeper地址

```properties
#修改
dubbo.registry.address=zookeeper://192.168.236.135:2181
```

3.打包dubbo-admin

4.执行 dubbo-admin\target 下的dubbo-admin-0.0.1-SNAPSHOT.jar

java -jar dubbo-admin-0.0.1-SNAPSHOT.jar

【注意：zookeeper的服务一定要打开！】

执行完毕，访问 http://localhost:7001/ ， 输入登录账户和密码，默认的root/root；

登录成功后，查看界面

## 整合

创建一个空项目



在里面分别创建两个springboot模块, 勾选web依赖

provider-ticket:服务提供者

consumer-user:服务消费者





**步骤:**

1.提供者提供服务

​	1.导入依赖(很多情况下依赖会冲突,这是本人测试过的)

​	2.修改配置文件

​	3.在想要被注册的服务上面增加一个dubbo的注解:@Service

```xml
        <!--dubbo-->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>2.7.3</version>
        </dependency>

        <!--zookeeper的客户端工具(github的)-->
        <dependency>
            <groupId>com.github.sgroschupf</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.1</version>
        </dependency>

        <!-- 引入zookeeper -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.14</version>
            <!--排除这个slf4j-log4j12-->
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```



```properties
server:
  port: 8081

dubbo:
  application:
    #当前应用名字
    name: provider-ticket
  registry:
    #注册中心地址
    address: zookeeper://192.168.236.135:2181
  scan:
    #扫描指定包下服务
    base-packages: com.yese.service
```

```java
package com.yese.service;

public interface TicketService {
    String getTicket();
}

```

```java
package com.yese.service.impl;

import com.yese.service.TicketService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Service //将服务发布出去,项目一启动就自动注册到注册中心
@Component //使用了dubbo后尽量不要用@Service注解
public class TicketServiceImpl implements TicketService {

    @Override
    public String getTicket() {
        return "《三国演义》";
    }
}

```

2.消费者消费

​	1.导入依赖(同上)

​	2.修改配置文件

​	3.从远程注入服务(@Reference)

注意: **本来正常步骤是需要将服务提供者的接口打包，然后用pom文件导入，我们这里使用简单的方式，直接将服务的接口拿过来，路径必须保证正确，即和服务提供者相同；** 



```properties
server:
  port: 8082

dubbo:
  application:
    #消费者去哪里拿服务,需要暴露自己的名字
    name: provider-ticket
  registry:
    #注册中心地址
    address: zookeeper://192.168.236.135:2181
```

```java
package com.yese.service;

public interface UserService {
    String bugTicket();
}
```

将提供者的TicketService复制过来

```java
package com.yese.service.impl;

import com.yese.service.TicketService;
import com.yese.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service //注入到容器中,这里是spring的service
public class UserServiceImpl implements UserService {

    @Reference //远程引用指定的服务，他会按照全类名进行匹配，看谁给注册中心注册了这个全类名
    private TicketService ticketService;

    @Override
    public String bugTicket() {
        String ticket = ticketService.getTicket();
        System.out.println("在注册中心买到" + ticket);
        return ticket;
    }
}

```

```java
package com.yese.controller;

import com.yese.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("buy")
    public String bugTicket() {
        String ticket = userService.bugTicket();
        return ticket;
    }

}

```

分别启动两个项目(可在dubbo-admin观察)

启动成功后访问: http://localhost:8082/buy ,页面输出	 《三国演义》 

控制台打印:买到《三国演义》



整合成功!


