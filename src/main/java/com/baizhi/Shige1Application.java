package com.baizhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.baizhi.dao")
@SpringBootApplication
public class Shige1Application {

    public static void main(String[] args) {
        SpringApplication.run(Shige1Application.class, args);
    }

}

