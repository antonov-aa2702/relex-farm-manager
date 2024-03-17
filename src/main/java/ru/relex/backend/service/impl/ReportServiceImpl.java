package ru.relex.backend.service.impl;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.relex.backend.aop.annotation.Loggable;
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.dto.product.ReportDetailsDto;
import ru.relex.backend.entity.Product;
import ru.relex.backend.repository.FilterManufacturedProductRepository;
import ru.relex.backend.service.ProductDetailFilterValidator;
import ru.relex.backend.service.ReportService;
import ru.relex.backend.service.UserService;

import java.util.List;

/**
 * Реализация {@link ReportService}
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final FilterManufacturedProductRepository manufacturedProductRepository;
    private final ProductDetailFilterValidator productDetailFilterValidator;

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<ReportDetailsDto> getReportByProductDetailFilter(ProductDetailFilter productDetailFilter) {
        productDetailFilterValidator.validateProductDetailFilter(productDetailFilter);
        return manufacturedProductRepository.findReportByProductDetailFilter(productDetailFilter).stream()
                .map(this::getProductDetailsDto)
                .toList();
    }

    private ReportDetailsDto getProductDetailsDto(Tuple tuple) {
        Product product = tuple.get(0, Product.class);
        Integer count = tuple.get(1, Integer.class);
        return ReportDetailsDto.builder()
                .id(product.getId())
                .name(product.getName())
                .unit(product.getUnit().getName())
                .count(count)
                .build();
    }
}
