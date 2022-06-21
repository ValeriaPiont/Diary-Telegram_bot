package com.karazin.diary_bot.backend.services.impl;

import com.karazin.diary_bot.backend.entities.User;
import com.karazin.diary_bot.backend.exceptions.EntityIsNullException;
import com.karazin.diary_bot.backend.exceptions.EntityNotFoundException;
import com.karazin.diary_bot.backend.repositories.UserRepository;
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

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        log.info("User to save {}", user);
        if (Objects.isNull(user)) {
            throw new EntityIsNullException("Incoming user is null");
        }
        userRepository.save(user);
        log.info("User saved");
    }

    private User getUserByTelegramId(Long telegramId) {
        Optional<User> userOptional = userRepository.findByTelegramId(telegramId);
        return userOptional
                .orElseThrow(() -> new EntityNotFoundException("User with id " + telegramId + " not found"));
    }

    public BotState getUserBotStateByTelegramId(Long telegramId) {
        return userRepository.findBotStateByTelegramId(telegramId);
    }

    public boolean isExistsByTelegramId(Long telegramId) {
        return userRepository.existsUserByTelegramId(telegramId);
    }

    public void changeUserBotState(BotState botState, Long telegramId) {
        User user = getUserByTelegramId(telegramId);
        user.setBotState(botState);
        userRepository.save(user);
    }

    public void changeCurrentIdPostForCommand(Long chosenPostId, Long telegramId) {
        User user = getUserByTelegramId(telegramId);
        user.setCurrentIdPostForCommand(chosenPostId);
        userRepository.save(user);
    }

    public Long getCurrentIdPostForCommandById(Long telegramId) {
        return userRepository.findChosenPostIdByTelegramId(telegramId);
    }

}

