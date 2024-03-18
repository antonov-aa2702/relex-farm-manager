package ru.relex.backend.service;

import ru.relex.backend.dto.product.ManufacturedProductDto;
import ru.relex.backend.dto.product.WorkingNormDto;
import ru.relex.backend.exception.ResourceIllegalStateException;

public interface ManufacturedProductService {

    /**
     * Осуществляет добавление произведенного продукта в систему конкретным сотрудником.
     * <p>
     * После добавления продукта система показывает сколько продуктов нужно
     * собрать согласно норме и сколько произведено на текущий момент,
     * а также крайни срок добавления продукта.
     *
     * @param productDto продукт, который будет добавлен
     * @param employeeId идентификатор сотрудника, который добавляет продукт
     * @return данные, содержащие информацию о новом о состоянии рабочей нормы после добавления продукта
     * @throws ResourceIllegalStateException если сотрудник имеет роль владельца или для данного сотрудника
     *                                       не создана рабочая норма
     */
    WorkingNormDto produceProduct(ManufacturedProductDto productDto, Long employeeId)
            throws ResourceIllegalStateException;
}
