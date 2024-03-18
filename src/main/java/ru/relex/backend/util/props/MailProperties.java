package ru.relex.backend.util.props;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Конфигурация для работы с почтой
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.mail")
@Component
public class MailProperties {

    /**
     * Хост для работы с почтой
     */
    private String host;

    /**
     * Порт для работы с почтой
     */
    private int port;

    /**
     * Почта отправителя
     */
    private String username;

    /**
     * Специальный пароль
     */
    private String password;

    /**
     * Настройки для работы с почтой
     */
    private Properties properties;

}
