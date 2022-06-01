package com.karazin.diary_bot.bot.handlers.commands;

import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartCommand extends BotCommand {

    public StartCommand() {
        super("/start", "Start bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text(DefaultBotMessage.ABOUT.getMessage())
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}

