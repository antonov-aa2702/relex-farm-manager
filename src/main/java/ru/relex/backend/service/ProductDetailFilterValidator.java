package ru.relex.backend.service;

import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.exception.ResourceIllegalStateException;

/**
 * Интерфейс для валидации полей фильтра, который используется при получении отчетов.
 */
public interface ProductDetailFilterValidator {

    /**
     * Проверяет валидность фильтра для получения отчетов
     *
     * @param productDetailFilter фильтр, для которого производится проверка
     * @throws ResourceIllegalStateException если указан идентификатор продукта или сотрудника,
     *                                       которого нет в базе
     *                                       или сотрудник является владельцем
     */
    void validateProductDetailFilter(ProductDetailFilter productDetailFilter)
            throws ResourceIllegalStateException;
}
