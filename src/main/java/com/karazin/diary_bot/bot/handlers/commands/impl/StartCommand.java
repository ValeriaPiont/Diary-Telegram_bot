package com.karazin.diary_bot.bot.handlers.commands.impl;

import com.karazin.diary_bot.bot.handlers.commands.Command;
import com.karazin.diary_bot.bot.keyboard.ReplyKeyboardMaker;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Component(value = "startCommand")
public class StartCommand implements Command {

    private final ReplyKeyboardMaker replyKeyboardMaker;

    public StartCommand(ReplyKeyboardMaker replyKeyboardMaker) {
        this.replyKeyboardMaker = replyKeyboardMaker;
    }

    @Override
    public SendMessage execute(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(DefaultBotMessage.ABOUT.getMessage())
                .replyMarkup(replyKeyboardMaker.getMainMenuKeyboard())
                .build();
    }

}
