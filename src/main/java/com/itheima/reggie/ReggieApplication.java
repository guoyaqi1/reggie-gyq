package com.itheima.reggie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement //开启事务支持的注解
public class ReggieApplication {
    public static void main(String[] args){
        SpringApplication.run(ReggieApplication.class,args);
    }

}
