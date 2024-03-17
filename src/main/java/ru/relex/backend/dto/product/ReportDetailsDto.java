package ru.relex.backend.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Модель данных для представления отчета.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для отчета")
public class ReportDetailsDto {

    /**
     * Идентификатор продукта, для которого рассчитан отчет
     */
    private Long id;

    /**
     * Название продукта
     */
    private String name;

    /**
     * Название единицы измерения
     */
    private String unit;

    /**
     * Количество произведенного продукта
     */
    private Integer count;
}
