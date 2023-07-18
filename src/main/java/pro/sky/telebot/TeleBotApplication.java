package pro.sky.telebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // включаем планировщик
public class TeleBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeleBotApplication.class, args);
    }

}
