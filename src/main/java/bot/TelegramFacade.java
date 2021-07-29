package bot;

import cache.DataCache;
import cache.UserDataCache;
import handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.LinkedList;
import java.util.List;


public class TelegramFacade {
    private DataCache userDataCache = new UserDataCache();
    private final List<InputMessageHandler> messageHandlers;
    private BotStateContext botStateContext;
    private CallBackQueryFacade callBackQueryFacade = new CallBackQueryFacade(userDataCache);

    public TelegramFacade() {
        messageHandlers = new LinkedList<>();
        messageHandlers.add(new FilmsShowHandler(userDataCache));
        messageHandlers.add(new StartPageHandler(userDataCache));
        messageHandlers.add(new ShowInfoHandler(userDataCache));
        botStateContext = new BotStateContext(messageHandlers);
    }

    /**
     * Проверяем, не пустое ли сообщение/текстовое ли, передаем на обработку
     */
    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;
        Message message = update.getMessage();
        Logger log = LoggerFactory.getLogger(TelegramFacade.class);
        if (message != null && message.hasText()) {
            log.info("New message from User: {}, chatID: {}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        if (update.hasCallbackQuery()) {
            log.info("New callbackQuery from User: {}, with data: {}",
                    update.getCallbackQuery().getFrom().getUserName(), update.getCallbackQuery().getData());
            return callBackQueryFacade.processCallbackQuery(update.getCallbackQuery());
        }
        return replyMessage;
    }

    /**
     * Присваиваем боту для данного пользователя состояние, переходим в BotContext для подбора Handler'а
     */
    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        int userID = message.getFrom().getId();
        SendMessage replyMessage;
        BotState botState;
        switch (inputMessage) {
            case "/start":
                botState = BotState.START_PAGE;
                break;
            case "Фильмы":
                botState = BotState.SHOW_FILMS;
                break;
            case "Информация":
                botState = BotState.SHOW_INFO;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userID);
        }
        userDataCache.setUsersCurrentBotState(userID, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }

    public SendPhoto handleUpdateWithPhoto(Update update) {
        SendPhoto replyMessage = null;
        Message message = update.getMessage();
        BotState botState;
        Logger log = LoggerFactory.getLogger(TelegramFacade.class);
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatID:{}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            int userID = message.getFrom().getId();
            botState = BotState.SHOW_FILMS;
            userDataCache.setUsersCurrentBotState(userID, botState);
            replyMessage = botStateContext.processInputMessagePhoto(botState, message);
        }
        return replyMessage;
    }
}