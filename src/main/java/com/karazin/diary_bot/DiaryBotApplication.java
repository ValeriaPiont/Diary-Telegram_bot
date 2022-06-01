package com.karazin.diary_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DiaryBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiaryBotApplication.class, args);
    }

}
