package com.karazin.diary_bot.bot.util;

import lombok.Getter;

@Getter
public enum DefaultBotMessage {

    ENTER_NEW_POST("Enter your text of new post"),
    CHOOSE_POST("Choose post"),
    EMPTY_LIST("You haven’t added posts yet"),
    ABOUT("Hi! I'm note bot. Available actions:\n* Add new note\n" +
            "* Update note (all or part)\n* Delete note\n" +
            "* Update your note title (by default title is creation time)\n" +
            "* Get all notes\n" +
            "Use keyboard to work with bot."),
    DELETED("The post was deleted successfully"),
    ENTER_NEW_TITLE("Enter post title"),
    ENTER_NEW_TEXT_POST("Enter new text of post"),
    ENTER_ADDITIONAL_TEXT_POST("Enter additional text of post"),
    CREATED("Successfully created"),
    UPDATED("Successfully updated"),
    ADDED("Successfully added"),
    UNKNOWN_TEXT("I don't understand you :(. Try to use /show_all_posts command"),
    INVALID_TEXT("You should send me the text"),
    INVALID_TITLE("Title should be less then 50 characters");

    private String message;

    DefaultBotMessage(String message) {
        this.message = message;
    }

}
