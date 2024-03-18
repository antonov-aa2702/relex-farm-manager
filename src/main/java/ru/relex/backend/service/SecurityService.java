package ru.relex.backend.service;

import org.springframework.security.core.AuthenticationException;
import ru.relex.backend.dto.auth.JwtRequest;
import ru.relex.backend.dto.auth.JwtResponse;
import ru.relex.backend.dto.user.ResponseUserDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.exception.ResourceIllegalStateException;

/**
 * Интерфейс для предоставления методов для работы с пользователями
 * при входе в систему или регистрации нового пользователя
 */
public interface SecurityService {

    /**
     * Осуществляет аутентификацию пользователя в системе.
     *
     * @param jwtRequest данные о пользователе, который пытается войти в систему
     * @return данные о пользователе, который прошел аутентификацию
     * @throws AuthenticationException если произошла ошибка при аутентификации
     */
    JwtResponse login(JwtRequest jwtRequest) throws AuthenticationException;

    /**
     * Осуществляет регистрацию пользователя в системе.
     *
     * @param userDto данные о пользователе, который регистрируется
     * @return данные о пользователе, который прошел регистрацию
     * @throws ResourceIllegalStateException если пользователь уже существует
     */
    ResponseUserDto registration(UserDto userDto) throws ResourceIllegalStateException;
}
