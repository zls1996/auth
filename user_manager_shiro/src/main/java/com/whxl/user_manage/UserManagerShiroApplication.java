package com.whxl.user_manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.whxl")
@MapperScan("classpath:mappers/*.xml")
@SpringBootApplication
//开启会话
@EnableTransactionManagement
@EnableCaching
public class UserManagerShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagerShiroApplication.class, args);
    }

}

