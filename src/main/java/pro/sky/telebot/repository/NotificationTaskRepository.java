package pro.sky.telebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telebot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {


    List<NotificationTask> findAllByTimestamp(LocalDateTime localDateTime);

    List<NotificationTask> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<NotificationTask> findAllByTimestampAndAndChatId(LocalDateTime localDateTime, Long chatId);
}
