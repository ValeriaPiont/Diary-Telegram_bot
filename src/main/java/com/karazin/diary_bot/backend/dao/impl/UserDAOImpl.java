package com.karazin.diary_bot.backend.dao.impl;

import com.karazin.diary_bot.backend.dao.UserDAO;
import com.karazin.diary_bot.backend.dao.util.EntityManagerPerform;
import com.karazin.diary_bot.backend.exceptions.EntityIsNullException;
import com.karazin.diary_bot.backend.models.User;
import com.karazin.diary_bot.bot.util.BotState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO {

    private final EntityManagerPerform entityManagerPerform;

    @Autowired
    public UserDAOImpl(EntityManagerPerform entityManagerPerform) {
        this.entityManagerPerform = entityManagerPerform;
    }

    public User save(User user) {
        entityManagerPerform.perform(entityManager -> entityManager.persist(entityManager.merge(user)));
        return user;
    }

    public Optional<User> findByTelegramId(Long telegramId) {
        return Optional.ofNullable(entityManagerPerform.performWith(entityManager -> entityManager.createQuery("select p from User p where p.telegramId = :telegramId", User.class)
                .setParameter("telegramId", telegramId)
                .getSingleResult()
        ));
    }

    public boolean userIsExistByTelegramId(Long telegramId) {
        if (Objects.isNull(telegramId)) {
            throw new EntityIsNullException("User id is null");
        }
        Optional<User> optionalUser = findByTelegramId(telegramId);
        return optionalUser.isPresent();
    }

    @Override
    public BotState getBotStateById(Long telegramId) {
        return entityManagerPerform.performWith(entityManager -> entityManager.createQuery("select p.botState from User p where p.telegramId = :telegramId", BotState.class)
                .setParameter("telegramId", telegramId)
                .getSingleResult()
        );
    }

    @Override
    public Long getCurrentIdPostForCommandById(Long telegramId) {
        return entityManagerPerform.performWith(entityManager -> entityManager.createQuery("select p.currentIdPostForCommand from User p where p.telegramId = :telegramId", Long.class)
                .setParameter("telegramId", telegramId)
                .getSingleResult()
        );
    }

}
