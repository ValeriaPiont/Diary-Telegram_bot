package com.karazin.diary_bot.bot.handlers.commands.callbackCommands.impl;

import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.handlers.commands.callbackCommands.CallBackCommand;
import com.karazin.diary_bot.bot.util.BotState;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class RenameTitleCommand implements CallBackCommand {

    private final UserService userService;

    public RenameTitleCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage execute(Long postId, Long chatId) {
        userService.changeCurrentIdPostForCommand(postId, chatId);
        userService.changeUserBotState(BotState.WAIT_NEW_POST_NAME,
                chatId);
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(DefaultBotMessage.ENTER_NEW_TITLE.getMessage())
                .build();
    }

}
