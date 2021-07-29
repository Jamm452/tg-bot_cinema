package bot;

import handlers.DataBaseManager;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    private static DataBaseManager dataBase;

    public static void main(String[] args) throws TelegramApiException {
        dataBase = DataBaseManager.getInstance();
        try {
            dataBase.connectToDB();
        } catch (Exception e) {
            System.out.println("Подсоединение отклонено. Убедитесь, что создан туннель к базе данных с локальной машины");
        }
        Bot bot = new Bot("@ob6_intensive_bot", "1899372099:AAFl9gaDXJe9Jb9umAjOeo6klhgwvxGgOEg");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}