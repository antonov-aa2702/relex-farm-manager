package ru.relex.backend.service;

import ru.relex.backend.dto.product.UnitDto;
import ru.relex.backend.exception.ResourceIllegalStateException;

import java.util.List;

/**
 * Интерфейс для предоставления методов для работы с единицами измерениями
 */
public interface UnitService {

    /**
     * Возвращает единицу измерения по имени.
     *
     * @param unit имя единицы измерения
     * @return найденную единицу измерения
     * @throws ResourceIllegalStateException если единица измерения не найдена
     */
    UnitDto getByName(String unit) throws ResourceIllegalStateException;

    /**
     * Регистрирует новую единицу измерения.
     *
     * @param unitDto данные о новой единице измерения
     * @return зарегистрированную единицу измерения
     * @throws ResourceIllegalStateException если единица измерения уже существует
     */
    UnitDto create(UnitDto unitDto) throws ResourceIllegalStateException;

    /**
     * Возвращает список всех зарегистрированных единиц измерения.
     *
     * @return список всех зарегистрированных единиц измерения
     */
    List<UnitDto> getAll();
}
