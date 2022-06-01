package com.karazin.diary_bot.backend.services.impl;

import com.karazin.diary_bot.backend.dao.impl.UserDAOImpl;
import com.karazin.diary_bot.backend.exceptions.EntityIsNullException;
import com.karazin.diary_bot.backend.exceptions.EntityNotFoundException;
import com.karazin.diary_bot.backend.model.User;
import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.util.BotState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDAOImpl userDAO;

    @Autowired
    public UserServiceImpl(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    public void saveUser(User user) {
        if (Objects.isNull(user)) {
            throw new EntityIsNullException("Incoming user is null");
        }
        User savedUser = userDAO.save(user);
        log.info("Saved user: {}", savedUser);
    }

    public Long getIdUserByTelegramId(Long telegramId) {
        Optional<User> userOptional = userDAO.findByTelegramId(telegramId);
        User user = userOptional
                .orElseThrow(() -> new EntityNotFoundException("User with id " + telegramId + " not found"));
        return user.getId();
    }

    private User getUserByTelegramId(Long telegramId) {
        Optional<User> userOptional = userDAO.findByTelegramId(telegramId);
        return userOptional
                .orElseThrow(() -> new EntityNotFoundException("User with id " + telegramId + " not found"));
    }

    public BotState getUserBotStateByTelegramId(Long telegramId) {
        return userDAO.getBotStateById(telegramId);
    }

    public boolean isExistsByTelegramId(Long telegramId) {
        return userDAO.userIsExistByTelegramId(telegramId);
    }

    public void changeUserBotState(BotState botState, Long telegramId) {
        User user = getUserByTelegramId(telegramId);
        user.setBotState(botState);
        userDAO.save(user);
    }

    public void changeCurrentIdPostForCommand(Long chosenPostId, Long telegramId) {
        User user = getUserByTelegramId(telegramId);
        user.setCurrentIdPostForCommand(chosenPostId);
        userDAO.save(user);
    }

    public Long getCurrentIdPostForCommandById(Long telegramId) {
        return userDAO.getCurrentIdPostForCommandById(telegramId);
    }

}

