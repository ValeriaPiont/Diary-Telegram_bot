package com.karazin.diary_bot.bot.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultBotReplyButton {

    START_COMMAND("/start"),
    ADD_REPLY_BUTTON("Add post"),
    SHOW_REPLY_BUTTON("Show all posts");

    private String buttonData;

}
