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
        SendMessage.SendMessageBuilder sendMessage =  SendMessage.builder()
                .chatId(chatId.toString())
                .text(checkCommand(chatId));
        userService.changeUserBotState(BotState.WAIT_FOR_COMMAND, chatId);
        return sendMessage.build();
    }

    private String checkCommand(Long chatId) {
        BotState botState = userService.getUserBotStateByTelegramId(chatId);
        if(botState.equals(BotState.WAIT_FOR_COMMAND)) {
            return DefaultBotMessage.BACK_NOTHING.getMessage();
        }
        return DefaultBotMessage.BACK_DONE.getMessage();
    }

}
