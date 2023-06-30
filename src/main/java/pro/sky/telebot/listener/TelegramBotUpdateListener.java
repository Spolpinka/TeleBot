package pro.sky.telebot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramBotUpdateListener implements UpdatesListener {

    Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);

    private final TelegramBot telegramBot;

    public TelegramBotUpdateListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init(){
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        try {
            list.forEach(update -> {
                logger.info("Handles update: ", update.message().text());

                Message message = update.message();

                Long id = message.chat().id();
                String text = message.text();

                if ("/start".equals(text)) {
                    SendMessage sendMessage = new SendMessage(id, "Hah, start... start by yourself");
                    SendResponse sendResponse = telegramBot.execute(sendMessage);
                    if (!sendResponse.isOk()) {
                        logger.error("Error while sending message {}", sendResponse.description());
                    }
                }



            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }



        return CONFIRMED_UPDATES_ALL;
    }
}
