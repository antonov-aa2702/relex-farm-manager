package ru.relex.backend.service.impl;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.relex.backend.aop.annotation.Loggable;
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.dto.product.AnalyticDetailsDto;
import ru.relex.backend.entity.Product;
import ru.relex.backend.entity.User;
import ru.relex.backend.repository.FilterWorkingNormRepository;
import ru.relex.backend.service.AnalyticsService;
import ru.relex.backend.service.ProductDetailFilterValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Реализация {@link AnalyticsService}
 */
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final FilterWorkingNormRepository workingNormRepository;
    private final ProductDetailFilterValidator productDetailFilterValidator;

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<AnalyticDetailsDto> getAnalyticsByProductDetailFilter(ProductDetailFilter productDetailFilter) {
        productDetailFilterValidator.validateProductDetailFilter(productDetailFilter);
        List<Tuple> analytics = workingNormRepository.findAnalyticsByProductDetailFilter(productDetailFilter);
        return getAnalyticDetailsDtos(analytics);
    }

    @Override
    @Loggable
    public Map<Long, Double> calculatingEfficiencyByEmployees(List<AnalyticDetailsDto> analytics) {
        return analytics.stream()
                .collect(Collectors.groupingBy(
                        AnalyticDetailsDto::getEmployeeId,
                        Collectors.averagingDouble(AnalyticDetailsDto::getTotalScore)));
    }

    private List<AnalyticDetailsDto> getAnalyticDetailsDtos(List<Tuple> analytics) {
        return analytics.stream().map(tuple -> {
            Integer totalWorkingNorm = tuple.get(2, Integer.class);
            Integer totalCurrentNorm = tuple.get(3, Integer.class);
            Double totalScore = getTotalScoreRounded(totalCurrentNorm, totalWorkingNorm);
            return getAnalyticDetailsDto(tuple, totalWorkingNorm, totalCurrentNorm, totalScore);
        }).toList();
    }

    private AnalyticDetailsDto getAnalyticDetailsDto(Tuple tuple, Integer totalWorkingNorm, Integer totalCurrentNorm, Double totalScore) {
        return AnalyticDetailsDto.builder().employeeId(tuple.get(0, User.class).getId()).email(tuple.get(0, User.class).getEmail()).productId(tuple.get(1, Product.class).getId()).totalWorkingNorm(totalWorkingNorm).totalCurrentNorm(totalCurrentNorm).totalScore(totalScore).build();
    }

    private Double getTotalScoreRounded(Integer totalCurrentNorm, Integer totalWorkingNorm) {
        double result = ((double) totalCurrentNorm / totalWorkingNorm) * 100;
        return Math.round(result * 100.0) / 100.0;
    }
}
