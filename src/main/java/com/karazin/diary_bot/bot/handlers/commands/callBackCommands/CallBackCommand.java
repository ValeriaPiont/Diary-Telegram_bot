package com.karazin.diary_bot.bot.handlers.commands.callBackCommands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CallBackCommand {
    SendMessage execute(Long postId, Long chatId);
}
