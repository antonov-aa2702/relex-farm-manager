package ru.relex.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.relex.backend.controller.handler.GlobalExceptionHandler;
import ru.relex.backend.exception.ResourceIllegalStateException;

import java.io.IOException;

/**
 * Класс, ответственный за обработку аутентификации JWT в HTTP-запросах.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Фильтрует входящие HTTP-запросы, проверяет токен JWT и настраивает проверку
     * подлинности Spring Security, если токен действителен.
     * <p>
     * В случае успешной аутентификации, устанавливает в Spring Security контекст
     * объект аутентификации, в случае ошибки, если пользователь не найден в системе,
     * записывает в лог сообщение о неуспешной аутентификации и не устанавливает в Spring Security
     * контекст объект аутентификации. Это означает, что при дальнейшей обработке запроса другими фильтрами
     * возникнет исключение, связанное с неправильным входным данным пользователя, которое будет обработано
     * в {@link GlobalExceptionHandler} и пользователь получит ответ, что он не прошел аутентификацию.
     *
     * @param request     входящий HTTP-запрос.
     * @param response    ответный HTTP-запрос.
     * @param filterChain цепочка фильтров для обработки запроса
     * @throws IOException,      если в процессе фильтрации возникает
     *                           ошибка ввода-вывода
     * @throws ServletException, если обработка запроса завершена
     *                           с ошибкой по какой-либо другой причине
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ResourceIllegalStateException exception) {
                log.info("Failed to set user authentication: {}", exception.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
