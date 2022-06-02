package com.karazin.diary_bot.backend.services;

import com.karazin.diary_bot.backend.models.User;
import com.karazin.diary_bot.bot.util.BotState;

public interface UserService {

    void saveUser(User userDTO);

    BotState getUserBotStateByTelegramId(Long id);

    boolean isExistsByTelegramId(Long telegramId);

    void changeUserBotState(BotState botState, Long telegramId);

    void changeCurrentIdPostForCommand(Long chosenPostId, Long telegramId);

    Long getCurrentIdPostForCommandById(Long telegramId);

}
