package ru.relex.backend.util;

import lombok.NoArgsConstructor;
import ru.relex.backend.dto.product.AnalyticDetailsDto;
import ru.relex.backend.dto.product.ReportDetailsDto;

import java.util.List;
import java.util.Map;

import static ru.relex.backend.entity.Product_.NAME;
import static ru.relex.backend.entity.Product_.UNIT;
import static ru.relex.backend.entity.WorkingNorm_.CURRENT_COUNT;
import static ru.relex.backend.entity.WorkingNorm_.PRODUCT;
import static ru.relex.backend.entity.WorkingNorm_.USER;
import static ru.relex.backend.entity.WorkingNorm_.WORKING_COUNT;

/**
 * Класс для работы с CSV.
 * Используется для формирования CSV на основании отчетных данных.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CsvUtil {

    public static final String TOTAL_COUNT = "totalCount";
    private static final String EFFECTIVENESS = "efficiency(%s)";

    public static String getCsvFromAnalyticDetails(List<AnalyticDetailsDto> analytics) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(String.format("%s,%s,%s,%s,%s\n",
                USER,
                PRODUCT,
                WORKING_COUNT,
                CURRENT_COUNT,
                TOTAL_COUNT
        ));
        for (AnalyticDetailsDto analyticsDetail : analytics) {
            csvContent.append(analyticsDetail.getEmployeeId()).append(",")
                    .append(analyticsDetail.getProductId()).append(",")
                    .append(analyticsDetail.getTotalWorkingNorm()).append(",")
                    .append(analyticsDetail.getTotalCurrentNorm()).append(",")
                    .append(analyticsDetail.getTotalScore())
                    .append("\n");
        }
        return csvContent.toString();
    }


    public static String getCsvFromReportDetails(List<ReportDetailsDto> report) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(String.format("%s,%s,%s,%s\n",
                PRODUCT,
                NAME,
                UNIT,
                TOTAL_COUNT
        ));
        for (ReportDetailsDto reportDetailsDto : report) {
            csvContent.append(reportDetailsDto.getId()).append(",")
                    .append(reportDetailsDto.getName()).append(",")
                    .append(reportDetailsDto.getUnit()).append(",")
                    .append(reportDetailsDto.getCount()).append("\n");
        }
        return csvContent.toString();
    }

    public static String getCsvFromEfficiencyByEmployees(Map<Long, Double> efficiencyByEmployees) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(String.format("%s,%s\n",
                USER,
                EFFECTIVENESS
        ));
        for (Map.Entry<Long, Double> longDoubleEntry : efficiencyByEmployees.entrySet()) {
            csvContent.append(longDoubleEntry.getKey()).append(",")
                    .append(longDoubleEntry.getValue())
                    .append("\n");
        }
        return csvContent.toString();
    }
}
