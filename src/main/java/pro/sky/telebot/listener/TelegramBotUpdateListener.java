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
import pro.sky.telebot.model.NotificationTask;
import pro.sky.telebot.service.NotificationTaskService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdateListener implements UpdatesListener {

    private final NotificationTaskService notificationTaskService;

    private final TelegramBot telegramBot;

    private final Pattern pattern = Pattern.compile(
            "(\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{2})" +
            " ([А-я\\d\\s.,!?;:]+)");

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("dd:MM:yyyy HH:mm");

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);


    public TelegramBotUpdateListener(NotificationTaskService notificationTaskService, TelegramBot telegramBot) {
        this.notificationTaskService = notificationTaskService;
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

                } else if ("/старт".equals(text)) {
                    sendMessage(id, """
                            _Значит, смотри\\!_
                            здесь можно фиксировать себе напоминалки
                            *для этого отправь сюда сообщение в формате:*
                            __30\\.06\\.2023 23:00 \\{название своей задачи\\}__                         
                            """);

                } else if (text != null) {
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        //обработка задания
                        Timestamp timestamp = parseDateTime(matcher.group(1));
                        String innerText = matcher.group(2);
                        NotificationTask notificationTask = new NotificationTask();
                        notificationTask.setMessage(innerText);
                        notificationTask.setTimestamp(timestamp);
                        notificationTask.setChatId(id);
                        //сохранение задания
                        notificationTaskService.save(notificationTask);
                        //отбивка, что задание сохранено
                        logger.info("Saved notification task: {}", notificationTask);

                    } else {
                        //отбивка, что не соответствует шаблону
                        sendMessage(id, "Не найден установленный формат сообщения\\!");

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
    private Timestamp parseDateTime(String dateTime) {
        try {
            Timestamp dateTime1 =  Timestamp.valueOf(LocalDateTime.parse(dateTime, dateTimeFormatter));
            logger.info("Parsed date time: {}", dateTime1);
            return dateTime1;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
