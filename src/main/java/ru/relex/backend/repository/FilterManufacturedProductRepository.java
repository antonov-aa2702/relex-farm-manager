package ru.relex.backend.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.util.QPredicates;

import java.util.List;

/**
 * Класс предназначен для получения информации о произведенных товарах по фильтру.
 */
public interface FilterManufacturedProductRepository {

    /**
     * Осуществляет поиск отчетной информации о произведенных товарах по фильтру.
     * <p>
     * В качестве полей фильтрации используется {@link ProductDetailFilter}.
     * Обязательные поля фильтра: {@link ProductDetailFilter#startTime}, {@link ProductDetailFilter#endTime}.
     * Дополнительные поля фильтра: {@link ProductDetailFilter#employeeId}, {@link ProductDetailFilter#productId}.
     * <p>
     * При осуществлении выборки происходит группировка по полю {@link ProductDetailFilter#productId}, а в качестве
     * агрегирующего значения используется количество произведенных единиц товара. Временной фильтр может быть любым (в том числе конкретный день, месяц и тд).
     * В случае присутствия полей фильтрации по продукту и/или сотруднику добавляется ограничение на полученную выборку.
     * <p>
     * Для динамического составления предиката используется {@link QPredicates}. Составление предиката динамически становится возможным
     * благодаря использованию библиотеки QueryDSL. Данный подход по сравнению со встроенным решением с помощью Criteria Api
     * помогает значительно увеличить читаемость запроса, поскольку сам запрос составлен на основе {@link JPAQuery}, который
     * имеет SQL-подобный синтаксис.
     *
     * @param productDetailFilter объект для осуществления фильтрации
     * @return список кортежей с информацией о произведенных единицах товаров
     */
    List<Tuple> findReportByProductDetailFilter(ProductDetailFilter productDetailFilter);
}
