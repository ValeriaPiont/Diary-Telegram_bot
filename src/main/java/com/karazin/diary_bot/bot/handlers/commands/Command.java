package com.karazin.diary_bot.bot.handlers.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Command {
    SendMessage execute(Long chatId);
}
