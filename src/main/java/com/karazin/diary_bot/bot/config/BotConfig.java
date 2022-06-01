package com.karazin.diary_bot.bot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class BotConfig {

    @Value("${telegram.username}")
    private String username;

    @Value("${telegram.token}")
    private String token;

    @Value("${telegram.webhook-path}")
    private String path;

}

