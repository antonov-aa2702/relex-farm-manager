package ru.relex.backend.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.repository.FilterWorkingNormRepository;
import ru.relex.backend.util.QPredicates;

import java.util.List;

import static ru.relex.backend.entity.QWorkingNorm.workingNorm;

/**
 * Реализация интерфейса {@link FilterWorkingNormRepository}
 */
@RequiredArgsConstructor
@Repository
public class FilterWorkingNormRepositoryImpl implements
        FilterWorkingNormRepository {

    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Tuple> findAnalyticsByProductDetailFilter(ProductDetailFilter productDetailFilter) {
        Predicate predicate = QPredicates.builder()
                .add(productDetailFilter.getEmployeeId(), workingNorm.user.id::eq)
                .add(productDetailFilter.getProductId(), workingNorm.product.id::eq)
                .add(productDetailFilter.getStartTime(), workingNorm.deadline::goe)
                .add(productDetailFilter.getEndTime(), workingNorm.deadline::loe)
                .buildAnd();
        return new JPAQuery<Tuple>(entityManager)
                .select(workingNorm.user,
                        workingNorm.product,
                        workingNorm.workingCount.sum(),
                        workingNorm.currentCount.sum())
                .from(workingNorm)
                .groupBy(workingNorm.user, workingNorm.product)
                .where(predicate)
                .fetch();
    }
}
