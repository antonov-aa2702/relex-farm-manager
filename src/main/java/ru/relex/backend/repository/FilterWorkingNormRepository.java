package ru.relex.backend.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.util.QPredicates;

import java.util.List;

/**
 * Класс предназначен для получения аналитической информации на основании рабочих норм.
 */
public interface FilterWorkingNormRepository {

    /**
     * Осуществляет поиск аналитической информации на основании рабочих норм.
     * <p>
     * В качестве полей фильтрации используется {@link ProductDetailFilter}.
     * Обязательные поля фильтра: {@link ProductDetailFilter#startTime}, {@link ProductDetailFilter#endTime}.
     * Дополнительные поля фильтра: {@link ProductDetailFilter#employeeId}, {@link ProductDetailFilter#productId}.
     * <p>
     * При осуществлении выборки происходит группировка по полям {@link ProductDetailFilter#productId}
     * и {@link ProductDetailFilter#employeeId}, а качестве агрегирующего значения
     * суммируются общее необходимое количество по сбору товара
     * на основании рабочих норм и фактическое суммарное текущее количество собранных товаров.
     * Временной фильтр может быть любым (в том числе конкретный день, месяц и тд).
     * В случае присутствия полей фильтрации по продукту и/или сотруднику
     * добавляется ограничение на полученную выборку.
     * <p>
     * Для динамического составления предиката используется {@link QPredicates}. Составление предиката
     * динамически становится возможным благодаря использованию библиотеки QueryDSL. Данный подход по сравнению со
     * встроенным решением с помощью Criteria Api помогает значительно увеличить читаемость запроса,
     * поскольку сам запрос составлен на основе {@link JPAQuery}, который имеет SQL-подобный синтаксис.
     *
     * @param productDetailFilter объект для осуществления фильтрации
     * @return список кортежей с информацией о сотруднике, продукте и общем количестве рабочей нормы и
     * общего фактически количества собранных единиц
     */
    List<Tuple> findAnalyticsByProductDetailFilter(ProductDetailFilter productDetailFilter);
}
