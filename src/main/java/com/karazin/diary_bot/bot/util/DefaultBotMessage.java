package com.karazin.diary_bot.bot.util;

import lombok.Getter;

@Getter
public enum DefaultBotMessage {

    ENTER_NEW_POST("Enter your text of new post"),
    CHOOSE_POST("Choose post"),
    EMPTY_LIST("You haven’t added posts yet"),
    ABOUT("${telegram.about}"),
    DELETED("The post was deleted successfully"),
    ENTER_NEW_TITLE("Enter post title"),
    ENTER_NEW_TEXT_POST("Enter new text of post"),
    ENTER_ADDITIONAL_TEXT_POST("Enter additional text of post"),
    CREATED("Successfully created"),
    UPDATED("Successfully updated"),
    ADDED("Successfully added"),
    UNKNOWN_TEXT("I don't understand you :(. Try to use /show_all_posts command"),
    INVALID_INPUT("You should send me the text");

    private String message;

    DefaultBotMessage(String message) {
        this.message = message;
    }

}
