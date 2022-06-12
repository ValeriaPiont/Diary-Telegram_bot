package com.karazin.diary_bot.bot.handlers.commands.replyKeyboardCommands.impl;

import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.handlers.commands.replyKeyboardCommands.Command;
import com.karazin.diary_bot.bot.util.BotState;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class AddPostCommand implements Command {

    private final UserService userService;

    public AddPostCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage execute(Long telegramId) {
        userService.changeUserBotState(BotState.WAIT_CONTENT_FOR_NOTE, telegramId);
        return SendMessage.builder()
                .chatId(telegramId.toString())
                .text(DefaultBotMessage.ENTER_NEW_POST.getMessage()).build();
    }

}
