package com.karazin.diary_bot.bot.handlers.commands.callbackCommands.impl;

import com.karazin.diary_bot.backend.services.PostService;
import com.karazin.diary_bot.bot.handlers.commands.callbackCommands.CallBackCommand;
import com.karazin.diary_bot.bot.keyboard.InlineKeyboardMaker;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class ShowButtonCommand implements CallBackCommand {

    private final PostService postService;
    private final InlineKeyboardMaker inlineKeyboardMaker;

    public ShowButtonCommand(PostService postService, InlineKeyboardMaker inlineKeyboardMaker) {
        this.postService = postService;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
    }

    @Override
    public SendMessage execute(Long postId, Long chatId) {
        String text = postService.getPostById(postId).getText();
        return SendMessage.builder().text(text)
                .chatId(String.valueOf(chatId))
                .replyMarkup(inlineKeyboardMaker.getPostKeyboard(postId))
                .build();
    }



}
