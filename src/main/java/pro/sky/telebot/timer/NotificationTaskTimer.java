package pro.sky.telebot.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telebot.service.NotificationTaskService;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationTaskTimer {

    private final NotificationTaskService notificationTaskService;
    private final TelegramBot telegramBot;

    public NotificationTaskTimer(NotificationTaskService notificationTaskService, TelegramBot telegramBot) {
        this.notificationTaskService = notificationTaskService;
        this.telegramBot = telegramBot;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void checkNotificationTasks() {
        notificationTaskService.findAllByTimestamp(LocalDateTime.now()
                .truncatedTo(java.time.temporal.ChronoUnit.MINUTES))
                .forEach(notificationTask -> {
                    telegramBot.execute(new SendMessage(notificationTask.getChatId(), notificationTask.getMessage()));
                    notificationTaskService.delete(notificationTask);
                });

    }
}
