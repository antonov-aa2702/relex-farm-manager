package ru.relex.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.relex.backend.dto.validation.Creatable;
import ru.relex.backend.entity.Role;

/**
 * Модель данных для пользователя.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных пользователя")
public class UserDto {

    /**
     * Идентификатор пользователя
     */
    private Long id;

    /**
     * Имя
     */
    @NotNull(message = "Имя не может отсутствовать", groups = Creatable.class)
    @Length(min = 1, max = 32, message = "Длина имени должна быть меньше 32 символов", groups = Creatable.class)
    private String firstName;

    /**
     * Фамилия
     */
    @NotNull(message = "Фамилия не может отсутствовать", groups = Creatable.class)
    @Length(min = 1, max = 32, message = "Фамилия должна быть меньше 32 символов", groups = Creatable.class)
    private String middleName;

    /**
     * Отчество
     */
    @NotNull(message = "Отчество не может отсутствовать", groups = Creatable.class)
    @Length(min = 1, max = 32, message = "Отчество должна быть меньше 32 символов", groups = Creatable.class)
    private String lastName;

    /**
     * Электронный адрес
     */
    @NotNull(message = "Электронный адрес не может отсутствовать", groups = Creatable.class)
    @Email(message = "Неправильный формат электронного адреса", groups = Creatable.class)
    @Length(min = 5, max = 32, message = "Электронный адрес должен содержать от 5 до 32 символов",
            groups = Creatable.class)
    private String email;

    /**
     * Пароль
     */
    @NotNull(message = "Пароль не может отсутствовать", groups = Creatable.class)
    @Length(min = 5, max = 32, message = "Пароль должен содержать от 5 до 32 символов", groups = Creatable.class)
    private String password;

    /**
     * Статус аккаунта пользователя
     */
    private Boolean enabled;

    /**
     * Роль пользователя
     */
    private Role role;
}
