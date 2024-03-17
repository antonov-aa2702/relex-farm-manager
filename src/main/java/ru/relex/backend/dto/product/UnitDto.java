package ru.relex.backend.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;
import ru.relex.backend.dto.validation.Creatable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotNull(message = "Название не может отсутствовать", groups = Creatable.class)
    @Length(min = 1, max = 16, message = "Название должно содержать от 1 до 16 символов", groups = Creatable.class)
    private String name;
}


