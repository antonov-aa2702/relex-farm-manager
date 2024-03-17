package ru.relex.backend.integration.repository;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import ru.relex.backend.annotation.IntegrationTest;
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.entity.Product;
import ru.relex.backend.repository.ManufacturedProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(value = "classpath:sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@IntegrationTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RequiredArgsConstructor
class FilterManufacturedProductRepositoryFilterTest {

    private final ManufacturedProductRepository manufacturedProductRepository;

    @Test
    void testFindReportByProductDetailFilterWithOnlyTime() {
        ProductDetailFilter productDetailFilter = ProductDetailFilter.builder()
                .startTime(LocalDate.now().atStartOfDay())
                .endTime(LocalDateTime.now().plusHours(24))
                .build();

        List<Tuple> actualResult =
                manufacturedProductRepository.findReportByProductDetailFilter(productDetailFilter);

        assertThat(actualResult).hasSize(2);
        List<Long> productIds = actualResult.stream()
                .map(tuple -> tuple.get(0, Product.class).getId())
                .toList();
        assertThat(productIds).contains(1L, 2L);
        List<Integer> values = actualResult.stream()
                .map(tuple -> tuple.get(1, Integer.class))
                .toList();
        assertThat(values).contains(35, 11);
    }

    @Test
    void testFindReportByProductDetailFilterWithProductId() {
        ProductDetailFilter productDetailFilter = ProductDetailFilter.builder()
                .startTime(LocalDate.now().atStartOfDay())
                .endTime(LocalDateTime.now().plusHours(24))
                .productId(1L)
                .build();

        List<Tuple> actualResult =
                manufacturedProductRepository.findReportByProductDetailFilter(productDetailFilter);

        assertThat(actualResult).hasSize(1);
        List<Long> productIds = actualResult.stream()
                .map(tuple -> tuple.get(0, Product.class).getId())
                .toList();
        assertThat(productIds).contains(1L);
        List<Integer> values = actualResult.stream()
                .map(tuple -> tuple.get(1, Integer.class))
                .toList();
        assertThat(values).contains(35);
    }

    @Test
    void testFindReportByProductDetailFilterWithEmployeeId() {
        ProductDetailFilter productDetailFilter = ProductDetailFilter.builder()
                .startTime(LocalDate.now().atStartOfDay())
                .endTime(LocalDateTime.now().plusHours(24))
                .employeeId(2L)
                .build();

        List<Tuple> actualResult =
                manufacturedProductRepository.findReportByProductDetailFilter(productDetailFilter);

        assertThat(actualResult).hasSize(1);
        List<Long> productIds = actualResult.stream()
                .map(tuple -> tuple.get(0, Product.class).getId())
                .toList();
        assertThat(productIds).contains(1L);
        List<Integer> values = actualResult.stream()
                .map(tuple -> tuple.get(1, Integer.class))
                .toList();
        assertThat(values).contains(25);
    }

    @Test
    void testFindReportByProductDetailFilterWithEmployeeIdAndProductId() {
        ProductDetailFilter productDetailFilter = ProductDetailFilter.builder()
                .startTime(LocalDate.now().atStartOfDay())
                .endTime(LocalDateTime.now().plusHours(24))
                .productId(2L)
                .employeeId(3L)
                .build();

        List<Tuple> actualResult =
                manufacturedProductRepository.findReportByProductDetailFilter(productDetailFilter);

        assertThat(actualResult).hasSize(1);
        List<Long> productIds = actualResult.stream()
                .map(tuple -> tuple.get(0, Product.class).getId())
                .toList();
        assertThat(productIds).contains(2L);
        List<Integer> values = actualResult.stream()
                .map(tuple -> tuple.get(1, Integer.class))
                .toList();
        assertThat(values).contains(11);
    }
}