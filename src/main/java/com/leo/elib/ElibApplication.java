package com.leo.elib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ElibApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElibApplication.class, args);
    }
}