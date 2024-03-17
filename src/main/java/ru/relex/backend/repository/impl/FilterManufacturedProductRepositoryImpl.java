package ru.relex.backend.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.repository.FilterManufacturedProductRepository;
import ru.relex.backend.util.QPredicates;

import java.util.List;

import static ru.relex.backend.entity.QManufacturedProduct.manufacturedProduct;

/**
 * Реализация интерфейса {@link FilterManufacturedProductRepository}
 */
@Repository
@RequiredArgsConstructor
public class FilterManufacturedProductRepositoryImpl implements
        FilterManufacturedProductRepository {

    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Tuple> findReportByProductDetailFilter(ProductDetailFilter productDetailFilter) {
        Predicate predicate = QPredicates.builder()
                .add(productDetailFilter.getEmployeeId(), manufacturedProduct.user.id::eq)
                .add(productDetailFilter.getProductId(), manufacturedProduct.product.id::eq)
                .add(productDetailFilter.getStartTime(), manufacturedProduct.time::goe)
                .add(productDetailFilter.getEndTime(), manufacturedProduct.time::loe)
                .buildAnd();
        return new JPAQuery<Tuple>(entityManager)
                .select(manufacturedProduct.product, manufacturedProduct.count.sum())
                .from(manufacturedProduct)
                .groupBy(manufacturedProduct.product)
                .where(predicate)
                .orderBy(manufacturedProduct.count.sum().desc())
                .fetch();
    }
}
