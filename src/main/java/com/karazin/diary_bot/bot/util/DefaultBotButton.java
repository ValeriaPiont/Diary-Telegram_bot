package com.karazin.diary_bot.bot.util;

import lombok.Getter;

@Getter
public enum DefaultBotButton {

    RENAME_TITLE_BUTTON("rename-title ", "Rename title"),
    ADD_TEXT_BUTTON("add-text ", "Add text to post"),
    UPDATE_TEXT_BUTTON("update-text ", "Rewrite post"),
    DELETE_BUTTON("delete ", "Delete"),
    SHOW_BUTTON("show ", "Show");

    private String callbackData;
    private String buttonData;

    DefaultBotButton(String callbackData, String buttonData) {
        this.callbackData = callbackData;
        this.buttonData = buttonData;
    }

}
