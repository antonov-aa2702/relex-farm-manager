package ru.relex.backend.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Модель данных для производства продукта.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Модель данных для производства продукта")
public class ManufacturedProductDto {

    /**
     * Идентификатор сущности
     */
    private Long id;

    /**
     * Идентификатор продукта, который будет произведен
     */
    @NotNull(message = "Идентификатор продукта не может быть пустым")
    private Long productId;

    /**
     * Количество производимого продукта
     */
    @NotNull(message = "Количество не может быть пустым")
    @Min(value = 1, message = "Количество должно быть больше 0")
    private Integer count;

    /**
     * Время производства продукта, устанавливается в момент создания
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;
}
