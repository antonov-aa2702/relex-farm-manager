package ru.relex.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.relex.backend.entity.Role;

/**
 * Модель данных для предоставления информации о пользователе.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных пользователя после регистрации.")
public class ResponseUserDto {

    /**
     * Идентификатор пользователя
     */
    private Long id;

    /**
     * Электронный адрес пользователя
     */
    private String email;
    /**
     * Статус акканта пользователя
     */
    private Boolean enabled;

    /**
     * Роль пользователя
     */
    private Role role;
}
