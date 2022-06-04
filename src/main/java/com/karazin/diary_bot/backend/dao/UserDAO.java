package com.karazin.diary_bot.backend.dao;

import com.karazin.diary_bot.backend.entities.User;
import com.karazin.diary_bot.bot.util.BotState;

import java.util.Optional;

/**
 * defines and API of Data-Access Object for entity User
 */
public interface UserDAO {

    /**
     * Saves user into databse and sets an id
     *
     * @param user new user
     */
    void save(User user);

    /**
     * Retrieves a user from the database by its Telegram id
     *
     * @param telegramId user Telegram (chat) id
     * @return user optional instance
     */
    Optional<User> findByTelegramId(Long telegramId);

    /**
     * Checks if the user is in the database
     *
     * @param telegramId user Telegram (chat) id
     * @return boolean
     */
    boolean userIsExistByTelegramId(Long telegramId);

    /**
     * Retrieves a user bot state from the database by  Telegram id
     *
     * @param telegramId user Telegram (chat) id
     * @return botstate instance
     */
    BotState getBotStateById(Long telegramId);

    /**
     * Retrieves a post id that was chosen by user for updating by Telegram id
     *
     * @param telegramId user Telegram (chat) id
     * @return Long
     */
    Long getCurrentIdPostForCommandById(Long telegramId);

}
