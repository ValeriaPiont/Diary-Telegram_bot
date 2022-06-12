package com.karazin.diary_bot.backend.repositories;

import com.karazin.diary_bot.backend.entities.User;
import com.karazin.diary_bot.bot.util.BotState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(Long telegramId);

    @Query("select u.botState from User u where u.telegramId = ?1")
    BotState findBotStateByTelegramId(Long telegramId);

    boolean existsUserByTelegramId(Long telegramId);

    @Query("select p.currentIdPostForCommand from User p where p.telegramId = ?1")
    Long findChosenPostIdByTelegramId(Long telegramId);

}
