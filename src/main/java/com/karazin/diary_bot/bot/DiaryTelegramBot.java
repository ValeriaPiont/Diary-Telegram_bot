package com.karazin.diary_bot.bot;

import com.karazin.diary_bot.backend.entities.User;
import com.karazin.diary_bot.backend.services.PostService;
import com.karazin.diary_bot.backend.services.UserService;
import com.karazin.diary_bot.bot.config.BotConfig;
import com.karazin.diary_bot.bot.config.client.TelegramClient;
import com.karazin.diary_bot.bot.exceptions.DiaryTelegramBotException;
import com.karazin.diary_bot.bot.handlers.CallbackHandler;
import com.karazin.diary_bot.bot.handlers.MessageHandler;
import com.karazin.diary_bot.bot.handlers.commands.AddPostCommand;
import com.karazin.diary_bot.bot.handlers.commands.ShowAllPostsCommand;
import com.karazin.diary_bot.bot.handlers.commands.StartCommand;
import com.karazin.diary_bot.bot.keyboard.ReplyKeyboardMaker;
import com.karazin.diary_bot.bot.util.BotState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class DiaryTelegramBot extends TelegramWebhookBot {

    private final BotConfig config;
    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;
    private final UserService userService;
    private final PostService postService;
    private final CommandRegistry commandRegistry;
    private final ReplyKeyboardMaker replyKeyboardMaker;

    public DiaryTelegramBot(BotConfig config, MessageHandler messageHandler, CallbackHandler callbackHandler,
                            UserService userService, PostService postService, TelegramClient telegramClient,
                            ReplyKeyboardMaker replyKeyboardMaker) {
        this.config = config;
        this.messageHandler = messageHandler;
        this.callbackHandler = callbackHandler;
        this.userService = userService;
        this.postService = postService;
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.commandRegistry = new CommandRegistry(true, this::getBotUsername);
        if (telegramClient.setWebhook(getBotToken(), getBotPath()).getStatusCode() == HttpStatus.OK) {
            log.info("Webhook was set");
        }
        addCommands();
        log.info("All commands was registered");
    }

    public void addCommands() {
        this.commandRegistry.register(new StartCommand(replyKeyboardMaker));
        this.commandRegistry.register(new AddPostCommand(userService));
        this.commandRegistry.register(new ShowAllPostsCommand(postService));
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
            if (update.getMessage().isCommand()) {
                this.commandRegistry.executeCommand(this, message);
            } else {
                messageHandler.handle(update.getMessage(), this);
            }
        } else if (update.hasCallbackQuery()) {
            execute(callbackHandler.handle(update.getCallbackQuery()));
        }
        return null;
    }

    private void processUser(Long telegramId, String username) {
        if (!userService.isExistsByTelegramId(telegramId)) {
            User user = new User();
            user.setBotState(BotState.WAIT_FOR_COMMAND);
            user.setTelegramId(telegramId);
            user.setUsername(username);
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
