package pro.sky.telebot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification_tasks")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", chatId=" + chatId +
                ", timestamp=" + timestamp +
                '}';
    }
}
