package com.karazin.diary_bot.bot.handlers.commands.replyKeyboardCommands.impl;

import com.karazin.diary_bot.backend.entities.Post;
import com.karazin.diary_bot.backend.services.PostService;
import com.karazin.diary_bot.bot.handlers.commands.replyKeyboardCommands.Command;
import com.karazin.diary_bot.bot.keyboard.InlineKeyboardMaker;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
public class ShowAllPostsCommand implements Command {

    private final PostService postService;

    private final InlineKeyboardMaker inlineKeyboardMaker;

    public ShowAllPostsCommand(PostService postService, InlineKeyboardMaker inlineKeyboardMaker) {
        this.postService = postService;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
    }

    @Override
    public SendMessage execute(Long telegramId) {
        List<Post> posts = postService.getAllPostsByUserTelegramId(telegramId);
        return SendMessage.builder()
                .chatId(telegramId.toString())
                .text(!posts.isEmpty() ? DefaultBotMessage.CHOOSE_POST.getMessage()
                        : DefaultBotMessage.EMPTY_LIST.getMessage())
                .replyMarkup(inlineKeyboardMaker.getAllPostsKeyboard(posts))
                .build();
    }

}

