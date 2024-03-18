package ru.relex.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * Модель данных для аутентификации пользователя.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для аутентификации пользователя")
public class JwtRequest {

    /**
     * Электронный адрес, по которому производится аутентификация пользователя
     */
    @NotNull(message = "Электронный адрес не может отсутствовать")
    @Email(message = "Неправильный формат электронного адреса")
    private String email;

    /**
     * Пароль, по которому производится аутентификация пользователя
     */
    @NotNull(message = "Пароль не может отсутствовать")
    @Length(min = 5, max = 32, message = "Пароль должен содержать от 5 до 32 символов")
    private String password;
}
