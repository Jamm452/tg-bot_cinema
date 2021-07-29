package handlers;

import bot.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandler {
    SendMessage handleAsMessage(Message message);

    SendPhoto handleAsPhoto(Message message);

    BotState getHandlerName();
}
