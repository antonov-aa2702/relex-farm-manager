package ru.relex.backend.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * Модель данных для единицы измерения.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для единицы измерения.")
public class UnitDto {

    /**
     * Идентификатор
     */
    private Integer id;

    /**
     * Название единицы измерения
     */
    @NotNull(message = "Название не может отсутствовать")
    @Length(min = 1, max = 16, message = "Название должно содержать от 1 до 16 символов")
    private String name;
}


