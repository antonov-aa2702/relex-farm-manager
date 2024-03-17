package ru.relex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.relex.backend.aop.annotation.Loggable;
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.dto.product.AnalyticDetailsDto;
import ru.relex.backend.dto.product.ReportDetailsDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.MailType;
import ru.relex.backend.service.AnalyticsService;
import ru.relex.backend.service.DailyGeneratorReportService;
import ru.relex.backend.service.EmailService;
import ru.relex.backend.service.ReportService;
import ru.relex.backend.service.UserService;
import ru.relex.backend.util.CsvUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Реализация {@link DailyGeneratorReportService}
 */
@Service
@RequiredArgsConstructor
public class DailyGeneratorReportServiceImpl implements DailyGeneratorReportService {

    @Value("${owner.email}")
    private String emailOwner;

    private final AnalyticsService analyticsService;
    private final ReportService reportService;
    private final EmailService emailService;
    private final UserService userService;

    @Override
    @Scheduled(cron = "${spring.scheduled.cron.expression.daily}")
    @Loggable
    public void generateDailyProductReport() {
        ProductDetailFilter filterForToday = getProductDetailFilterForToday();
        List<ReportDetailsDto> report = reportService
                .getReportByProductDetailFilter(filterForToday);
        String csvContent = CsvUtil.getCsvFromReportDetails(report);
        emailService.sendEmail(emailOwner, MailType.DAILY_PRODUCT_REPORT, csvContent);
    }

    @Override
    @Scheduled(cron = "${spring.scheduled.cron.expression.daily}")
    @Loggable
    public void generateDailyAnalyticReport() {
        ProductDetailFilter filterForToday = getProductDetailFilterForToday();
        List<AnalyticDetailsDto> analytics = analyticsService
                .getAnalyticsByProductDetailFilter(filterForToday);
        String csvContent = CsvUtil.getCsvFromAnalyticDetails(analytics);
        emailService.sendEmail(emailOwner, MailType.DAILY_EMPLOYEE_ANALYTICS, csvContent);
    }

    @Override
    @Scheduled(cron = "${spring.scheduled.cron.expression.daily}")
    @Loggable
    public void generateDailyAnalyticEmployeesReport() {
        ProductDetailFilter filterForToday = getProductDetailFilterForToday();
        List<AnalyticDetailsDto> analytics = analyticsService
                .getAnalyticsByProductDetailFilter(filterForToday);
        Map<Long, Double> efficiencyByEmployees = analyticsService
                .calculatingEfficiencyByEmployees(analytics);
        String csvContent = CsvUtil.getCsvFromEfficiencyByEmployees(efficiencyByEmployees);
        emailService.sendEmail(emailOwner, MailType.DAILE_EMPLOYEE_SCORE_ANALYTICS, csvContent);
    }

    @Override
    @Scheduled(cron = "${spring.scheduled.cron.expression.daily}")
    @Loggable
    public void generateDailyEmployeeScoreReport() {
        ProductDetailFilter filterForToday = getProductDetailFilterForToday();
        List<AnalyticDetailsDto> analytics = analyticsService
                .getAnalyticsByProductDetailFilter(filterForToday);
        analyticsService.calculatingEfficiencyByEmployees(analytics)
                .forEach((employeeId, efficiency) -> {
                    UserDto userDto = userService.getById(employeeId);
                    if (userDto.getEnabled()) {
                        emailService.sendEmail(userDto.getEmail(),
                                MailType.DAILY_EMPLOYEE_SCORE,
                                String.valueOf(efficiency));
                    }
                });
    }

    private ProductDetailFilter getProductDetailFilterForToday() {
        return ProductDetailFilter.builder()
                .startTime(LocalDate.now().atStartOfDay())
                .endTime(LocalDateTime.now().plusHours(24))
                .build();
    }
}
