package com.karazin.diary_bot.bot.config.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "telegram", url = "https://api.telegram.org")
public interface TelegramClient {

    @GetMapping("/bot${telegram.token}/setWebhook?url=${telegram.webhook-path}")
    ResponseEntity<?> setWebhook(@PathVariable String token, @PathVariable String webhookPath);

}
