package ru.relex.backend.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.relex.backend.entity.User;

import java.util.Collection;
import java.util.Collections;

/**
 * Класс для получения объекта {@link JwtEntity} из {@link User}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtEntityFactory {

    /**
     * Метод создает объект для хранения данных о пользователе, который вошел в систему с помощью JWT.
     *
     * @param user пользователь, который вошел в систему с помощью JWT
     * @return объект для хранения данных в системе
     */
    public static JwtEntity create(User user) {
        return JwtEntity
                .builder()
                .id(user.getId())
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(getAuthorities(user))
                .enabled(user.getEnabled())
                .build();
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    }
}
