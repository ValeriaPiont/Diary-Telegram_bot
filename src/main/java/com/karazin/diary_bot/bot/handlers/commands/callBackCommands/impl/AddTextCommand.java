package com.karazin.diary_bot.bot.handlers.commands.callBackCommands.impl;

import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.handlers.commands.callBackCommands.CallBackCommand;
import com.karazin.diary_bot.bot.util.BotState;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
@Component
public class AddTextCommand implements CallBackCommand {

    private final UserService userService;

    public AddTextCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage execute(Long postId, Long chatId) {
        userService.changeCurrentIdPostForCommand(postId, chatId);
        userService.changeUserBotState(BotState.WAIT_NEW_CONTENT_FOR_NOTE_ADD,
                chatId);
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(DefaultBotMessage.ENTER_ADDITIONAL_TEXT_POST.getMessage())
                .build();
    }

}
