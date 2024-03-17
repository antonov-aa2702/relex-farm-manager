package ru.relex.backend.service;

import ru.relex.backend.entity.MailType;

/**
 * Интерфейс для предоставления методов для рассылки электронных писем.
 */
public interface EmailService {
    /**
     * Предоставляет возможность отправить электронное письмо.
     * При возникновении ошибки при отправке письма в лог записывается
     * сообщение об ошибке, которое должно оперативно отслеживаться
     * администратором системы.
     *
     * @param email    электронная почта получателя
     * @param mailType тип письма
     * @param content  строковое представление содержимого письма
     */
    void sendEmail(String email, MailType mailType, String content);
}
