package com.karazin.diary_bot.bot.handlers.commands.callBackCommands.impl;

import com.karazin.diary_bot.backend.services.PostService;
import com.karazin.diary_bot.bot.handlers.commands.callBackCommands.CallBackCommand;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class DeletePostCommand implements CallBackCommand {

    private final PostService postService;

    public DeletePostCommand(PostService postService) {
        this.postService = postService;
    }

    @Override
    public SendMessage execute(Long postId, Long chatId) {
        postService.deletePost(postId);
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(DefaultBotMessage.DELETED.getMessage())
                .build();
    }

}
