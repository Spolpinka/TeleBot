package pro.sky.telebot.service;

import org.springframework.stereotype.Service;
import pro.sky.telebot.model.NotificationTask;
import pro.sky.telebot.repository.NotificationTaskRepository;

@Service
public class NotificationTaskService {
    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void save(NotificationTask notificationTask) {
        notificationTaskRepository.save(notificationTask);
    }


}
