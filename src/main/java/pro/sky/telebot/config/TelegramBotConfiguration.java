package pro.sky.telebot.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {
    private String key;

    @Bean
    public TelegramBot telegramBot(@Value("${telebot.key}") String key){
        return new TelegramBot(key);
    }
}
