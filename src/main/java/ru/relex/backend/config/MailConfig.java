package ru.relex.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.relex.backend.util.props.MailProperties;


/**
 * Класс для конфигурации почтового сервиса.
 */
@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final MailProperties mailProperties;

    /**
     * Определяет конфигурацию для почтового сервиса.
     * Настройки почтового сервиса берутся из {@link MailProperties}, которые
     * находятся в файле application.yaml.
     *
     * @return конфигурация почтового сервиса
     */
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailProperties.getHost());
        javaMailSender.setPort(mailProperties.getPort());
        javaMailSender.setUsername(mailProperties.getUsername());
        javaMailSender.setPassword(mailProperties.getPassword());
        javaMailSender.setJavaMailProperties(mailProperties.getProperties());
        return javaMailSender;
    }
}
