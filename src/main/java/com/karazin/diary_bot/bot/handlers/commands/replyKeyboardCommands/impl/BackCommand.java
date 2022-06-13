package com.karazin.diary_bot.bot.handlers.commands.replyKeyboardCommands.impl;

import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.handlers.commands.replyKeyboardCommands.Command;
import com.karazin.diary_bot.bot.util.BotState;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class BackCommand implements Command {

    private final UserService userService;

    public BackCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage execute(Long chatId) {
//        userService.getUserBotStateByTelegramId(chatId); // get current state
        userService.changeUserBotState(BotState.WAIT_FOR_COMMAND, chatId);
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Backkkkk").build();
    }

}
