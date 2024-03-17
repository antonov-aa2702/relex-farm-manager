package ru.relex.backend.security.expression;

import ru.relex.backend.security.JwtEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Класс для создания выражений для проверки прав при выполнении запросов
 */
@Service("customSecurityExpression")
public class CustomSecurityExpression {

    /**
     * Проверяет, является доступ пользователя с переданным id
     * разрешенным.
     * <p>
     * Метод определен только для аутентифицированного пользователя.
     * Для проверки из контекста извлекается текущий аутентифицированный пользователь,
     * его идентификатор сравнивается с переданным id. В случае несовпадения
     * пользователь получит сообщение о запрещённом доступе. Метод используется
     * для запрета доступа к ресурсам других пользователей.
     *
     * @param id идентификатор пользователя, который пытается получить доступ к ресурсу
     * @return {@code true}, если пользователь с переданным id имеет права доступа к ресурсу, {@code false} в противном случае
     */
    public boolean canAccessEmployee(Long id) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        JwtEntity principal = (JwtEntity) authentication.getPrincipal();
        return principal.getId().equals(id);
    }
}
