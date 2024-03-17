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
import ru.relex.backend.dto.validation.Creatable;
import ru.relex.backend.entity.Status;

import java.time.LocalDateTime;

/**
 * Модель данных для рабочей нормы.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для рабочей нормы")
public class WorkingNormDto {

    /**
     * Идентификатор
     */
    private Long id;

    /**
     * Идентификатор продукта, для которого будет сформирована рабочая норма
     */
    @NotNull(message = "Идентификатор продукта не может отсутствовать", groups = Creatable.class)
    private Long productId;

    /**
     * Идентификатор сотрудника, для которого будет сформирована рабочая норма
     */
    @NotNull(message = "Идентификатор сотрудника не может отсутствовать", groups = Creatable.class)
    private Long employeeId;

    /**
     * Необходимое рабочее количество по производству
     */
    @NotNull(message = "Размер рабочей нормы не может быть пустым", groups = Creatable.class)
    @Min(value = 1, message = "Размер рабочей нормы не может быть меньше 1", groups = Creatable.class)
    private Integer workingCount;

    /**
     * Текущее количество собранного продукта
     */
    private Integer currentCount;

    /**
     * Крайний срок выполнения рабочей нормы
     */
    @NotNull(message = "Срок выполнения рабочей нормы не может быть пустым", groups = Creatable.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;

    /**
     * Статус рабочей нормы
     */
    private Status status;
}
