package com.karazin.diary_bot.bot.handlers;

import com.karazin.diary_bot.backend.services.PostService;
import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.keyboard.InlineKeyboardMaker;
import com.karazin.diary_bot.bot.util.BotState;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class CallbackHandler {

    private final UserService userService;
    private final PostService postService;

    private final InlineKeyboardMaker inlineKeyboardMaker;

    public CallbackHandler(UserService userService, PostService postService, InlineKeyboardMaker inlineKeyboardMaker) {
        this.userService = userService;
        this.postService = postService;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
    }

    public SendMessage handle(CallbackQuery callbackQuery) {
        String query = callbackQuery.getData();
        String chatId = callbackQuery.getFrom().getId().toString();
        String command = query.split(" ")[0];
        long postId = Long.parseLong(query.split(" ")[1]);
        SendMessageBuilder sendMessage = SendMessage.builder().chatId(chatId);
        switch (command) {
            case "show":
                List<List<InlineKeyboardButton>> buttonsShow = inlineKeyboardMaker.getPostKeyboard(postId);
                String text = postService.getPostById(postId).getText();
                return sendMessage.text(text)
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboard(buttonsShow)
                                .build())
                        .build();
            case "delete":
                postService.deletePost(postId);
                return sendMessage.text(DefaultBotMessage.DELETED.getMessage())
                        .build();
            case "rename-title":
                userService.changeCurrentIdPostForCommand(postId, Long.valueOf(chatId));
                userService.changeUserBotState(BotState.WAIT_NEW_POST_NAME,
                        callbackQuery.getFrom().getId());
                return sendMessage.text(DefaultBotMessage.ENTER_NEW_TITLE.getMessage())
                        .build();
            case "update-text":
                userService.changeCurrentIdPostForCommand(postId, Long.valueOf(chatId));
                userService.changeUserBotState(BotState.WAIT_NEW_CONTENT_FOR_NOTE_UPDATE,
                        callbackQuery.getFrom().getId());
                return sendMessage.text(DefaultBotMessage.ENTER_NEW_TEXT_POST.getMessage())
                        .build();
            case "add-text":
                userService.changeCurrentIdPostForCommand(postId, Long.valueOf(chatId));
                userService.changeUserBotState(BotState.WAIT_NEW_CONTENT_FOR_NOTE_ADD,
                        callbackQuery.getFrom().getId());
                return sendMessage.text(DefaultBotMessage.ENTER_ADDITIONAL_TEXT_POST.getMessage())
                        .build();
        }
        return null;
    }


}
