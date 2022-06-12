package com.karazin.diary_bot.bot.handlers;

import com.karazin.diary_bot.bot.handlers.commands.callbackCommands.CallBackCommand;
import com.karazin.diary_bot.bot.handlers.commands.callbackCommands.impl.*;
import com.karazin.diary_bot.bot.util.DefaultBotButton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class CallbackHandler {

    private final ApplicationContext ctx;

    public CallbackHandler(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public SendMessage handle(CallbackQuery callbackQuery) {
        String query = callbackQuery.getData();
        String chatId = callbackQuery.getFrom().getId().toString();
        String command = query.split(" ")[0];
        Long postId = Long.parseLong(query.split(" ")[1]);
        CallBackCommand callBackCommand = determineCommand(command);
        if (Objects.nonNull(callBackCommand)) {
            return callBackCommand.execute(postId, Long.parseLong(chatId));
        }
        return null;
    }

    private CallBackCommand determineCommand(String command) {
        Optional<DefaultBotButton> defaultBotReplyButton = findCommandByMessageText(command);
        if (defaultBotReplyButton.isEmpty()) {
            return null;
        }
        return switch (defaultBotReplyButton.get()) {
            case SHOW_BUTTON -> ctx.getBean("showButtonCommand", ShowButtonCommand.class);
            case DELETE_BUTTON -> ctx.getBean("deletePostCommand", DeletePostCommand.class);
            case RENAME_TITLE_BUTTON -> ctx.getBean("renameTitleCommand", RenameTitleCommand.class);
            case UPDATE_TEXT_BUTTON -> ctx.getBean("updateTextCommand", UpdateTextCommand.class);
            case ADD_TEXT_BUTTON -> ctx.getBean("addTextCommand", AddTextCommand.class);
        };
    }

    private Optional<DefaultBotButton> findCommandByMessageText(String command) {
        return Arrays.stream(DefaultBotButton.values())
                .filter(val -> val.getCallbackData().trim().equals(command))
                .findFirst();
    }

}
