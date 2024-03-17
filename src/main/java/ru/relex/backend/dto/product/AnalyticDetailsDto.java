package ru.relex.backend.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Модель данных для представления данных аналитики.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для аналитики")
public class AnalyticDetailsDto {

    /**
     * Идентификатор сотрудника, для которого получена аналитика
     */
    private Long employeeId;

    /**
     * Электронный адрес сотрудника, для которого получена аналитика.
     * Поле не используется при отображении пользователю. Оно необходимо
     * для внутренней работы при отправке аналитики по электронной почте.
     */
    @JsonIgnore
    private String email;

    /**
     * Идентификатор продукта, для которого получена аналитика
     */
    private Long productId;

    /**
     * Суммарное необходимое количество по сбору
     * продукта {@link AnalyticDetailsDto#productId}
     * сотрудником {@link AnalyticDetailsDto#employeeId},
     */
    private Integer totalWorkingNorm;

    /**
     * Фактически суммарное собранное количество по сбору
     * продукта {@link AnalyticDetailsDto#productId}
     * сотрудником {@link AnalyticDetailsDto#employeeId}
     */
    private Integer totalCurrentNorm;

    /**
     * Значение по суммарной выработке рабочей нормы, определяемое
     * отношением {@link AnalyticDetailsDto#totalCurrentNorm}
     * к {@link AnalyticDetailsDto#totalWorkingNorm} и умноженное на 100
     */
    private Double totalScore;
}


