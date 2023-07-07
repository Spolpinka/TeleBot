package pro.sky.telebot.service;

import org.springframework.stereotype.Service;
import pro.sky.telebot.model.NotificationTask;
import pro.sky.telebot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationTaskService {
    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void save(NotificationTask notificationTask) {
        notificationTaskRepository.save(notificationTask);
    }

    //find all
    public List<NotificationTask> findAll() {
        return notificationTaskRepository.findAll();
    }

    //find all by timestamp
    public List<NotificationTask> findAllByTimestamp(LocalDateTime timestamp) {
        return notificationTaskRepository.findAllByTimestamp(timestamp);
    }

    //delete
    public void delete(NotificationTask notificationTask) {
        notificationTaskRepository.delete(notificationTask);
    }
}
