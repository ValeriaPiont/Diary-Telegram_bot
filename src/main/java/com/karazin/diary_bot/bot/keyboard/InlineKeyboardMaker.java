package com.karazin.diary_bot.bot.keyboard;

import com.karazin.diary_bot.backend.entities.Post;
import com.karazin.diary_bot.bot.util.DefaultBotButton;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardMaker {

    public InlineKeyboardMarkup getPostKeyboard(Long postId) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = InlineKeyboardButton
                .builder()
                .text(DefaultBotButton.RENAME_TITLE_BUTTON.getButtonData())
                .callbackData(DefaultBotButton.RENAME_TITLE_BUTTON.getCallbackData() + postId)
                .build();
        InlineKeyboardButton inlineKeyboardButton2 = InlineKeyboardButton
                .builder()
                .text(DefaultBotButton.ADD_TEXT_BUTTON.getButtonData())
                .callbackData(DefaultBotButton.ADD_TEXT_BUTTON.getCallbackData() + postId)
                .build();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = InlineKeyboardButton
                .builder()
                .text(DefaultBotButton.UPDATE_TEXT_BUTTON.getButtonData())
                .callbackData(DefaultBotButton.UPDATE_TEXT_BUTTON.getCallbackData() + postId).build();
        InlineKeyboardButton inlineKeyboardButton4 = InlineKeyboardButton
                .builder()
                .text(DefaultBotButton.DELETE_BUTTON.getButtonData())
                .callbackData(DefaultBotButton.DELETE_BUTTON.getCallbackData() + postId).build();

        keyboardButtonsRow2.add(inlineKeyboardButton3);
        keyboardButtonsRow2.add(inlineKeyboardButton4);

        buttons.add(keyboardButtonsRow1);
        buttons.add(keyboardButtonsRow2);

       return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

    public InlineKeyboardMarkup getAllPostsKeyboard(List<Post> posts) {
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        for (Post post : posts) {
            keyboardButtons.add(List.of(InlineKeyboardButton.builder()
                    .text(post.getTitle())
                    .callbackData(DefaultBotButton.SHOW_BUTTON.getCallbackData() + post.getId()).build()));
        }
        return InlineKeyboardMarkup.builder().keyboard(keyboardButtons).build();
    }

}
