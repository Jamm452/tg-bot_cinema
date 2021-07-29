package bot;

import handlers.DataBaseManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final TelegramFacade telegramFacade = new TelegramFacade();

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText() && update.getMessage().getText().equals("Фильмы")) {
            {
                DataBaseManager manager = DataBaseManager.getInstance();
                manager.getFilmsFromDB();
                for (int loopSize = manager.getListOfFilms().size(); loopSize > 0; loopSize--) {
                    SendPhoto answer = telegramFacade.handleUpdateWithPhoto(update);
                    try {
                        execute(answer);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            SendMessage answer = telegramFacade.handleUpdate(update);
            answer.enableMarkdown(true);
            try {
                execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}