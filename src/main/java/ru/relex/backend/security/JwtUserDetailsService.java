package ru.relex.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.relex.backend.entity.User;
import ru.relex.backend.service.UserService;

/**
 * Класс, предоставляющий Spring Security информацию о пользователе, который входит в систему.
 */
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Возвращает информацию о пользователе, который имеет такой же логин,
     * что и логин аутентифицированного пользователя.
     * <p>
     * При попытке пользователя получить доступ к ресурсам, которые требуют аутентификации,
     * вызывается автоматически для предоставления Spring Security информации о пользователе
     * и дальнейшей проверки введенных данных с теми, что хранятся в базе данных.
     *
     * @param username логин пользователя, который пытается войти в систему
     * @return объект для Spring Security, содержащий данные о пользователе, который найден в базе данных
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByEmail(username);
        return JwtEntityFactory.create(user);
    }
}
