package com.karazin.diary_bot.bot.handlers;

import com.karazin.diary_bot.backend.services.PostService;
import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.util.BotState;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

@Component
public class MessageHandler {

    private final UserService userService;
    private final PostService postService;

    public MessageHandler(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    public void handle(Message message, AbsSender sender) {
        Long telegramId = message.getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(telegramId));
        BotState botState = userService.getUserBotStateByTelegramId(telegramId);
        boolean changeBotState = true;
        try {
            String text = message.getText();
            if (Objects.isNull(text)) {
                sendMessage.setText(DefaultBotMessage.INVALID_TEXT.getMessage());
                sender.execute(sendMessage);
                return;
            }
            switch (botState) {
                case WAIT_CONTENT_FOR_NOTE:
                    postService.savePostText(text, telegramId);
                    sendMessage.setText(DefaultBotMessage.CREATED.getMessage());
                    break;
                case WAIT_NEW_POST_NAME:
                    boolean isUpdated = postService
                            .updatePostTitle(userService.getCurrentIdPostForCommandById(telegramId), text);
                    if (isUpdated) {
                        sendMessage.setText(DefaultBotMessage.UPDATED.getMessage());
                    } else {
                        sendMessage.setText(DefaultBotMessage.INVALID_TITLE.getMessage());
                        changeBotState = false;
                    }
                    break;
                case WAIT_NEW_CONTENT_FOR_NOTE_ADD:
                    postService.updatePostText(userService.getCurrentIdPostForCommandById(telegramId), text, false);
                    sendMessage.setText(DefaultBotMessage.ADDED.getMessage());
                    break;
                case WAIT_NEW_CONTENT_FOR_NOTE_UPDATE:
                    postService.updatePostText(userService.getCurrentIdPostForCommandById(telegramId), text, true);
                    sendMessage.setText(DefaultBotMessage.UPDATED.getMessage());
                    break;
                default:
                    sendMessage.setText(DefaultBotMessage.INVALID_TEXT.getMessage());
                    break;
            }
            if (changeBotState) {
                userService.changeUserBotState(BotState.WAIT_FOR_COMMAND, telegramId);
            }
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}

