package com.karazin.diary_bot.bot.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultBotMessage {

    ENTER_NEW_POST("✏Enter your text of new post"),
    CHOOSE_POST("Your posts:"),
    EMPTY_LIST("You haven’t added posts yet"),
    ABOUT("\uD83D\uDC4B Hi! I'm note bot. Available actions:\n\n\uD83D\uDC9C Add new post\n" +
            "\uD83D\uDDA4 Update post (or rewrite)\n\uD83D\uDC9C Delete note\n" +
            "\uD83D\uDDA4 Update post title (by default title is creation time)\n" +
            "\uD83D\uDC9C Get all posts\n\n" +
            "Use keyboard to work with bot."),
    DELETED("The post was deleted successfully✨"),
    ENTER_NEW_TITLE("✏Enter post title"),
    ENTER_NEW_TEXT_POST("✏Enter new text of post"),
    ENTER_ADDITIONAL_TEXT_POST("✏Enter additional text of post"),
    CREATED("Successfully created✨"),
    UPDATED("Successfully updated✨"),
    ADDED("Successfully added✨"),
    UNKNOWN_TEXT("I don't understand you\uD83D\uDE14. Try to use /show_all_posts command"),
    INVALID_TEXT("I don't understand you \uD83E\uDD7A"),
    INVALID_TITLE("❗Title should be less then 50 characters"),
    BACK_DONE("Canceled✨"),
    BACK_NOTHING("Nothing to cancel");

    private String message;

}
