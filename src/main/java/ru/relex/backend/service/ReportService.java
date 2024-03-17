package ru.relex.backend.service;

import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.dto.product.ReportDetailsDto;
import ru.relex.backend.exception.ResourceIllegalStateException;

import java.util.List;

/**
 * Интерфейс для предоставления методов при работе с отчетами.
 */
public interface ReportService {

    /**
     * Осуществляет выдачу отчета по фильтру. В качестве полей фильтра передается временный интервал,
     * а также опционально идентификатор продукта и/или идентификатор сотрудника.
     * <p>
     * Разрешено получать отчет за любой промежуток времени. В случае отсутствия
     * идентификатора продукта или сотрудника отчет выводится по работе всей фермы в целом.
     *
     * @param productDetailFilter фильтр для получения отчета
     * @return отчет
     * @throws ResourceIllegalStateException если указан идентификатор продукта или сотрудника, которого нет в базе
     *                                       или сотрудник имеет роль владельца
     */
    List<ReportDetailsDto> getReportByProductDetailFilter(ProductDetailFilter productDetailFilter)
            throws ResourceIllegalStateException;
}
