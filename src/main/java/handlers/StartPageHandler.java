package handlers;

import bot.BotState;
import cache.DataCache;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class StartPageHandler implements InputMessageHandler {
    private DataCache userDataCache;

    public StartPageHandler(DataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendPhoto handleAsPhoto(Message message) {
        return null;
    }

    @Override
    public SendMessage handleAsMessage(Message message) {
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START_PAGE;
    }

    private SendMessage processUserInput(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Привет! Я - бот, который поможет тебе забронировать места в кинотеатре! Нажми на кнопку 'Фильмы', чтобы посмотреть, какие фильмы есть в прокате!");
        sendMessage.setReplyMarkup(getReplyKeyboard());
        int userId = message.getFrom().getId();
        userDataCache.setUsersCurrentBotState(userId, BotState.START_PAGE);
        return sendMessage;
    }

    private ReplyKeyboardMarkup getReplyKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("Фильмы"));
        keyboard.add(keyboardFirstRow);

        keyboardSecondRow.add(new KeyboardButton("Информация"));
        keyboard.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}