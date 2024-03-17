package ru.relex.backend.security;

import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Класс для хранения информации о пользователе в контексте приложения,
 * который вошел в систему с помощью JWT.
 */
@Value
@Builder
public class JwtEntity implements UserDetails {

    /**
     * Идентификатор пользователя.
     */
    Long id;

    /**
     * Логин пользователя.
     */
    String username;

    /**
     * Пароль пользователя.
     */
    String password;

    /**
     * Список ролей пользователя.
     */
    Collection<? extends GrantedAuthority> authorities;

    /**
     * Статус аккаунта пользователя.
     */
    boolean enabled;

    /**
     * Проверяет, не истекло ли время действия аккаунта.
     *
     * @return {@code true}, означающее, что время действия аккаунта не истекло.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт. Для отслеживания статуса
     * аккаунта используется другое поле класса {@link UserDetails} {@code enabled},
     * говорящее о том, включен или отключен аккаунт в системе. Пользователь с
     * отключенным аккаунтом не может войти в систему.
     *
     * @return {@code true}, означающее, аккаунт не заблокирован.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истекло ли время действия пароля.
     *
     * @return {@code true}, означающее, что время действия пароля не истекло.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
