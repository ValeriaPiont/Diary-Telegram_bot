package com.karazin.diary_bot.bot;

import com.karazin.diary_bot.backend.entities.User;
import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.config.BotConfig;
import com.karazin.diary_bot.bot.config.client.TelegramClient;
import com.karazin.diary_bot.bot.exceptions.DiaryTelegramBotException;
import com.karazin.diary_bot.bot.handlers.CallbackHandler;
import com.karazin.diary_bot.bot.handlers.MessageHandler;
import com.karazin.diary_bot.bot.handlers.commands.impl.AddPostCommand;
import com.karazin.diary_bot.bot.handlers.commands.impl.ShowAllPostsCommand;
import com.karazin.diary_bot.bot.handlers.commands.impl.StartCommand;
import com.karazin.diary_bot.bot.handlers.commands.Command;
import com.karazin.diary_bot.bot.util.BotState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DiaryTelegramBot extends TelegramWebhookBot {

    private final BotConfig config;
    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;
    private final UserService userService;
    private final ApplicationContext ctx;

    public DiaryTelegramBot(BotConfig config, MessageHandler messageHandler, CallbackHandler callbackHandler,
                           TelegramClient telegramClient, UserService userService, ApplicationContext ctx) {
        this.config = config;
        this.messageHandler = messageHandler;
        this.callbackHandler = callbackHandler;
        this.userService = userService;
        if (telegramClient.setWebhook(getBotToken(), getBotPath()).getStatusCode() != HttpStatus.OK) {
            throw new DiaryTelegramBotException("Exception: webhook not set");
        }
        this.ctx = ctx;
        log.info(Arrays.stream(ctx.getBeanDefinitionNames()).collect(Collectors.toList()).toString());
        log.info("Webhook was set");
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (DiaryTelegramBotException | TelegramApiException e) {
            log.error("Error while executing message. Exception " + e.getMessage());
            return new SendMessage(update.getMessage().getChatId().toString(), "Something went wrong");
        }
    }

    public BotApiMethod<?> handleUpdate(Update update) throws TelegramApiException {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getFrom().getId();
            String username = message.getFrom().getFirstName();
            processUser(chatId, username);
            Command command = determineCommand(update.getMessage().getText());
            if (Objects.nonNull(command)) {
                execute(command.execute(update.getMessage().getChatId()));
            } else {
                messageHandler.handle(update.getMessage(), this);
            }
        } else if (update.hasCallbackQuery()) {
            execute(callbackHandler.handle(update.getCallbackQuery()));
        }
        return null;
    }

    private Command determineCommand(String command) {
        return switch (command) {
            case "/start" -> ctx.getBean("startCommand", StartCommand.class);
            case "/add_post" -> ctx.getBean("addPostCommand", AddPostCommand.class);
            case "/show_all_posts" -> ctx.getBean("showAllPostsCommand", ShowAllPostsCommand.class);
            default -> null;
        };
    }

    private void processUser(Long telegramId, String username) {
        if (!userService.isExistsByTelegramId(telegramId)) {
            User user = User.builder()
                    .botState(BotState.WAIT_FOR_COMMAND)
                    .telegramId(telegramId)
                    .username(username)
                    .build();
            userService.saveUser(user);
        }
    }

    @Override
    public String getBotPath() {
        return config.getPath();
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

}
