package pro.sky.telebot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdateListener implements UpdatesListener {

    private final Pattern pattern = Pattern.compile("(\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{2})\\s+" +
            " ([А-я\\d\\s.,!?;:]+)");

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("dd:MM:yyyy HH:mm");

    Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);

    private final TelegramBot telegramBot;

    public TelegramBotUpdateListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        try {
            list.forEach(update -> {
                logger.info("Handles update: ", update);

                Message message = update.message();

                Long id = message.chat().id();
                String text = message.text();

                if ("/start".equals(text)) {
                    sendMessage(id, """
                            Hah, start
                            ~maybe you'll do it by yourself~
                            __or not__
                            _или нет_
                            *а ты попробуй*
                            ||Fuck off||
                            [github] \\(https://github\\.com/Spolpinka\\)
                            """);

                }
                if ("/старт".equals(text)) {
                    sendMessage(id, """
                            _Значит, смотри\\!_
                            здесь можно фиксировать себе напоминалки
                            *для этого отправь сюда сообщение в формате:*
                            __30\\.06\\.2023 23:00 \\{название своей задачи\\}__                         
                            """);

                }
                if (text != null) {
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        //обработка задания
                        LocalDateTime dateTime = parseDateTime(matcher.group(1));
                        String innerText = matcher.group(2);
                    } else {
                        //отбивка, что не соответствует шаблону
                        sendMessage(id, "Не найден установленный формат сообщения!");

                    }
                }


            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


        return CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long id, String message) {
        SendMessage sendMessage = new SendMessage(id, message);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error while sending message {}", sendResponse.description());
        }

    }

    @Nullable
    private LocalDateTime parseDateTime(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
