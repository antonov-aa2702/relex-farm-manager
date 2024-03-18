package ru.relex.backend.service;

import ru.relex.backend.dto.filter.WorkingNormFilter;
import ru.relex.backend.dto.product.ManufacturedProductDto;
import ru.relex.backend.dto.product.WorkingNormDto;
import ru.relex.backend.entity.Status;
import ru.relex.backend.exception.ResourceIllegalStateException;

import java.util.List;

/**
 * Интерфейс для предоставления методов для работы с рабочими нормами
 */
public interface WorkingNormService {

    /**
     * Осуществляет создание рабочей нормы.
     * <p>
     * Запрещено добавлять рабочую норму задним числом.
     * Разрешается добавлять рабочую норму только на сегодняшний день.
     * Время установления рабочей нормы устанавливается в соответствии с требованиями
     * владельца фермы. Не допускается повторно добавлять рабочую норму для одного и того же
     * продукта и одного и того же сотрудника при действующей рабочей нормы для данного
     * продукта и сотрудника. После выполнения такой рабочей нормы это разрешается делать.
     * Рабочая норма создается только для активного аккаунта сотрудника.
     *
     * @param workingNormDto рабочая норма
     * @return созданная рабочая норма
     * @throws ResourceIllegalStateException если рабочая норма уже существует
     *                                       или указано неверное время
     *                                       или аккаунт сотрудника не активен
     */
    WorkingNormDto create(WorkingNormDto workingNormDto) throws ResourceIllegalStateException;

    /**
     * Осуществляет получение доступной рабочей нормы,
     * которая будет использована при добавлении продукта сотрудником.
     *
     * @param manufacturedProductDto продукт, который будет добавлен
     * @param employeeId             идентификатор сотрудника, которы будет добавлять продукт
     * @return рабочая норма, для которой будет добавлен продукт
     * @throws ResourceIllegalStateException если действующих рабочих норм для данного продукта и сотрудника нет
     */
    WorkingNormDto getAccessWorkingNorm(ManufacturedProductDto manufacturedProductDto, Long employeeId)
            throws ResourceIllegalStateException;

    /**
     * Осуществляет обновление показателей рабочей нормы
     * с добавлением некоторого количества произведенного продукта.
     *
     * @param workingNormDto рабочая норма, которая будет обновлена
     * @param count          количество произведенного продукта
     * @return обновленная рабочая норма
     * @throws ResourceIllegalStateException если не найдена рабочая норма
     */
    WorkingNormDto updateWorkingNorm(WorkingNormDto workingNormDto, Integer count)
            throws ResourceIllegalStateException;

    /**
     * Осуществляет получение списка рабочих для конкретного сотрудника по фильтру.
     *
     * @param employeeId        идентификатор сотрудника, для которого будут получены рабочие нормы
     * @param workingNormFilter фильтр для получения рабочих норм
     * @return список рабочих норм
     * @throws ResourceIllegalStateException если сотрудник является владельцем
     */
    List<WorkingNormDto> getWorkingNormsByFilter(Long employeeId, WorkingNormFilter workingNormFilter)
            throws ResourceIllegalStateException;

    /**
     * Осуществляет обновление рабочей нормы.
     *
     * @param workingNorm рабочая норма, подлежащая обновлению
     */
    void updateWorkingNorm(WorkingNormDto workingNorm);


    /**
     * Осуществляет получение списка рабочих норм по статусу.
     * <p>
     * Используется для анализа рабочих норм при обновлении статусов
     * после выполнения рабочей нормы или истечению срока выполнения.
     *
     * @param status статус
     * @return список рабочих норм
     */
    List<WorkingNormDto> getWorkingNormByStatus(Status status);
}
