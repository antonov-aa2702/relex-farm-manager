package ru.relex.backend.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Фильтр для получения отчета по собранным продуктам.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для фильтрации отчета")
public class ProductDetailFilter {

    /**
     * Начало периода для получения отчета
     */
    @NotNull(message = "Начало периода не может быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime startTime;

    /**
     * Конец периода для получения отчета
     */
    @NotNull(message = "Конец периода не может быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime endTime;

    /**
     * Идентификатор сотрудника, опциональное поле
     */
    public Long employeeId;

    /**
     * Идентификатор продукта, опциональное поле
     */
    public Long productId;
}
