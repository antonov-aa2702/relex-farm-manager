package ru.relex.backend.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.relex.backend.entity.MailType;
import ru.relex.backend.service.EmailService;

import java.time.LocalDate;

/**
 * Реализация {@link EmailService}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String DESCRIPTION_FOR_ANALYTICS = """
                Аналитическая информация включает данные об эффективности работы
                фермы на основании выданных норм. Данные представлены в виде таблицы.
                Колонки: сотрудник, товар, заданная норма, произведенное количество, эффективность(КПД в %).
                Если для одного сотрудника выдавалась норма сбора по одному и тому же продукту несколько раз в день,
                то происходит группировка и в отчете представлены суммарные значения.
                КПД рассчитывается как отношение собранной продукции к заданной норме.
            """;

    public static final String DESCRIPTION_FOR_EMPLOYEE_ANALYTICS = """
                Аналитическая информация включает данные об общей эффективности работы
                сотрудника за текущий день. Данные представлены в виде таблицы.
                Колонки: сотрудник, его эффективность(КПД в %).
                КПД рассчитывается как отношение собранной продукции к заданной норме.            
            """;

    public static final String DESCRIPTION_FOR_REPORT = """
                Отчетная информация включает данные о всех произведенных продуктов фермы
                за сегодняшний день. Данные представлены в виде таблицы.
                Колонки: Продукт, Название, Единица измерения, произведенное количество.
            """;

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String email, MailType mailType, String content) {
        switch (mailType) {
            case DAILY_PRODUCT_REPORT:
                sendDailyProductReport(email, content);
                break;
            case DAILY_EMPLOYEE_ANALYTICS:
                sendDailyEmployeeAnalytics(email, content);
                break;
            case DAILE_EMPLOYEE_SCORE_ANALYTICS:
                sendDailyEmployeeScoreAnalytics(email, content);
                break;
            case DAILY_EMPLOYEE_SCORE:
                sendDailyEmployeeScore(email, content);
                break;
        }
    }

    @SneakyThrows
    private void sendDailyEmployeeScoreAnalytics(String email, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(String.format("Аналитический отчет по эффективности всех работников фермы %s", LocalDate.now()));
            helper.setText(DESCRIPTION_FOR_EMPLOYEE_ANALYTICS);
            String fileName = String.format("analytics-employees_%s.csv", LocalDate.now());
            helper.addAttachment(fileName, new ByteArrayResource(content.getBytes()));
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error while sending email", e);
        }
    }

    private void sendDailyProductReport(String email, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(String.format("Отчетная информация о произведенных товаров на ферме за %s", LocalDate.now()));
            helper.setText(DESCRIPTION_FOR_REPORT);
            String fileName = String.format("report_%s.csv", LocalDate.now());
            helper.addAttachment(fileName, new ByteArrayResource(content.getBytes()));
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error while sending email: {}", e.getMessage());
        }
    }

    private void sendDailyEmployeeAnalytics(String email, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(String.format("Аналитическая информация по работе фермы за %s", LocalDate.now()));
            helper.setText(DESCRIPTION_FOR_ANALYTICS);
            String fileName = String.format("analytics_%s.csv", LocalDate.now());
            helper.addAttachment(fileName, new ByteArrayResource(content.getBytes()));
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error while sending email: {}", e.getMessage());
        }
    }

    @SneakyThrows
    private void sendDailyEmployeeScore(String email, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(String.format("Ваша оценка эффективности за %s", LocalDate.now()));
            helper.setText("КПД:" + content + "%");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error while sending email: {}", e.getMessage());
        }
    }
}
