package com.karazin.diary_bot.bot.handlers.commands;

import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.util.BotState;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AddPostCommand extends BotCommand {

    private final UserService userService;

    public AddPostCommand(UserService userService) {
        super("/add_post", "Add new post");
        this.userService = userService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        userService.changeUserBotState(BotState.WAIT_CONTENT_FOR_NOTE, user.getId());
        System.out.println(userService.getUserBotStateByTelegramId(user.getId()));
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text(DefaultBotMessage.ENTER_NEW_POST.getMessage()).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
