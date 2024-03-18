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
import ru.relex.backend.entity.User;
import ru.relex.backend.repository.UserRepository;
import ru.relex.backend.repository.WorkingNormRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(value = "classpath:sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@IntegrationTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FilterWorkingNormRepositoryFilterTest {

    private final WorkingNormRepository workingNormRepository;

    private final UserRepository userRepository;

    @Test
    public void testFindAnalyticsByProductDetailFilterWithOnlyTime() {
        System.out.println(userRepository.findByEmail("owner"));
        ProductDetailFilter productDetailFilter = ProductDetailFilter.builder()
                .startTime(LocalDate.now().atStartOfDay())
                .endTime(LocalDateTime.now().plusHours(24))
                .build();

        List<Tuple> actualResult = workingNormRepository
                .findAnalyticsByProductDetailFilter(productDetailFilter);

        assertThat(actualResult).hasSize(3);
        List<Long> employeeIds = actualResult.stream()
                .map(tuple -> tuple.get(0, User.class).getId())
                .toList();
        assertThat(employeeIds).contains(2L, 3L, 3L);
        List<Long> productIds = actualResult.stream()
                .map(tuple -> tuple.get(1, Product.class).getId())
                .toList();
        assertThat(productIds).contains(1L, 1L, 1L, 2L);
        List<Integer> workingCounts = actualResult.stream()
                .map(tuple -> tuple.get(2, Integer.class))
                .toList();
        assertThat(workingCounts).contains(25, 10, 11);
        List<Integer> currentCounts = actualResult.stream()
                .map(tuple -> tuple.get(3, Integer.class))
                .toList();
        assertThat(currentCounts).contains(16, 11, 20);
    }

    @Test
    public void testFindAnalyticsByProductDetailFilterWithProductId() {
        System.out.println(userRepository.findByEmail("owner"));
        ProductDetailFilter productDetailFilter = ProductDetailFilter.builder()
                .startTime(LocalDate.now().atStartOfDay())
                .endTime(LocalDateTime.now().plusHours(24))
                .productId(1L)
                .build();

        List<Tuple> actualResult = workingNormRepository
                .findAnalyticsByProductDetailFilter(productDetailFilter);

        assertThat(actualResult).hasSize(2);
        List<Long> employeeIds = actualResult.stream()
                .map(tuple -> tuple.get(0, User.class).getId())
                .toList();
        assertThat(employeeIds).contains(2L, 3L);
        List<Long> productIds = actualResult.stream()
                .map(tuple -> tuple.get(1, Product.class).getId())
                .toList();
        assertThat(productIds).contains(1L, 1L);
        List<Integer> workingCounts = actualResult.stream()
                .map(tuple -> tuple.get(2, Integer.class))
                .toList();
        assertThat(workingCounts).contains(25, 10);
        List<Integer> currentCounts = actualResult.stream()
                .map(tuple -> tuple.get(3, Integer.class))
                .toList();
        assertThat(currentCounts).contains(16, 11);
    }

    @Test
    public void testFindAnalyticsByProductDetailFilterWithEmployeeId() {
        System.out.println(userRepository.findByEmail("owner"));
        ProductDetailFilter productDetailFilter = ProductDetailFilter.builder()
                .startTime(LocalDate.now().atStartOfDay())
                .endTime(LocalDateTime.now().plusHours(24))
                .employeeId(2L)
                .build();

        List<Tuple> actualResult = workingNormRepository
                .findAnalyticsByProductDetailFilter(productDetailFilter);

        assertThat(actualResult).hasSize(1);
        List<Long> employeeIds = actualResult.stream()
                .map(tuple -> tuple.get(0, User.class).getId())
                .toList();
        assertThat(employeeIds).contains(2L);
        List<Long> productIds = actualResult.stream()
                .map(tuple -> tuple.get(1, Product.class).getId())
                .toList();
        assertThat(productIds).contains(1L);
        List<Integer> workingCounts = actualResult.stream()
                .map(tuple -> tuple.get(2, Integer.class))
                .toList();
        assertThat(workingCounts).contains(25);
        List<Integer> currentCounts = actualResult.stream()
                .map(tuple -> tuple.get(3, Integer.class))
                .toList();
        assertThat(currentCounts).contains(16);
    }


    @Test
    public void testFindAnalyticsByProductDetailFilterWithEmployeeIdAndProductId() {
        System.out.println(userRepository.findByEmail("owner"));
        ProductDetailFilter productDetailFilter = ProductDetailFilter.builder()
                .startTime(LocalDate.now().atStartOfDay())
                .endTime(LocalDateTime.now().plusHours(24))
                .employeeId(3L)
                .productId(2L)
                .build();

        List<Tuple> actualResult = workingNormRepository
                .findAnalyticsByProductDetailFilter(productDetailFilter);

        assertThat(actualResult).hasSize(1);
        List<Long> employeeIds = actualResult.stream()
                .map(tuple -> tuple.get(0, User.class).getId())
                .toList();
        assertThat(employeeIds).contains(3L);
        List<Long> productIds = actualResult.stream()
                .map(tuple -> tuple.get(1, Product.class).getId())
                .toList();
        assertThat(productIds).contains(2L);
        List<Integer> workingCounts = actualResult.stream()
                .map(tuple -> tuple.get(2, Integer.class))
                .toList();
        assertThat(workingCounts).contains(11);
        List<Integer> currentCounts = actualResult.stream()
                .map(tuple -> tuple.get(3, Integer.class))
                .toList();
        assertThat(currentCounts).contains(20);
    }
}