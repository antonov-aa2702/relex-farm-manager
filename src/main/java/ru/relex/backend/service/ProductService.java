package ru.relex.backend.service;

import ru.relex.backend.dto.product.ProductDto;
import ru.relex.backend.exception.ResourceIllegalStateException;

import java.util.List;

/**
 * Интерфейс для предоставления методов для работы с продуктами.
 */

public interface ProductService {


    /**
     * Возвращает продукт по идентификатору
     *
     * @param id идентификатор продукта
     * @return найденный продукт
     * @throws ResourceIllegalStateException если продукт не найден
     */
    ProductDto getById(Long id) throws ResourceIllegalStateException;

    /**
     * Осуществляет создание нового продукта.
     *
     * @param productDto продукт, который будет создан
     * @return созданный продукт
     * @throws ResourceIllegalStateException если продукт уже существует
     */
    ProductDto create(ProductDto productDto) throws ResourceIllegalStateException;

    /**
     * Возвращает список всех зарегистрированных продуктов.
     *
     * @return список всех зарегистрированных продуктов
     */
    List<ProductDto> getAll();
}
