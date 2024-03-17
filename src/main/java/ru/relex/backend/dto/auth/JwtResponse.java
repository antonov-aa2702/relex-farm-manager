package ru.relex.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Модель данных для входа в систему.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для входа в систему")
public class JwtResponse {
    /**
     * Идентификатор пользователя, который вошел в систему
     */
    private Long id;

    /**
     * Электронный адрес пользователя, который вошел в систему
     */
    private String email;

    /**
     * Сгенерированный токен доступа для работы в системе
     */
    private String token;
}
