package com.karazin.diary_bot.bot.handlers.commands;

import com.karazin.diary_bot.backend.model.Post;
import com.karazin.diary_bot.backend.services.PostService;
import com.karazin.diary_bot.bot.util.DefaultBotButton;
import com.karazin.diary_bot.bot.util.DefaultBotMessage;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class ShowAllPostsCommand extends BotCommand {

    private final PostService postService;

    public ShowAllPostsCommand(PostService postService) {
        super("/show_all_posts", "All posts");
        this.postService = postService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        List<Post> posts = postService.getAllPostsByUserTelegramId(user.getId());
        try {
            absSender
                    .execute(SendMessage
                            .builder()
                            .chatId(chat.getId().toString())
                            .text(!posts.isEmpty() ? DefaultBotMessage.CHOOSE_POST.getMessage()
                                    : DefaultBotMessage.EMPTY_LIST.getMessage())
                            .replyMarkup(getPostsKeyboard(posts))
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getPostsKeyboard(List<Post> posts) {
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        for (Post post : posts) {
            keyboardButtons.add(List.of(InlineKeyboardButton.builder()
                    .text(post.getTitle())
                    .callbackData(DefaultBotButton.SHOW_BUTTON.getCallbackData() + post.getId()).build()));
        }
        return InlineKeyboardMarkup.builder().keyboard(keyboardButtons).build();
    }

}
