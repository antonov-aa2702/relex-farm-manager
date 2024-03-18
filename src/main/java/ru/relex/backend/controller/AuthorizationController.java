package ru.relex.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.relex.backend.dto.auth.JwtRequest;
import ru.relex.backend.dto.auth.JwtResponse;
import ru.relex.backend.dto.user.ResponseUserDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.dto.validation.Creatable;
import ru.relex.backend.service.SecurityService;

/**
 * Контроллер, который обрабатывает HTTP-запросы, связанные с аутентификацией и регистрацией.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authorization", description = "Authorization API")
public class AuthorizationController {

    private final SecurityService securityService;

    /**
     * Выполняет аутентификацию пользователя. Для аутентификации пользователь вводит логин и пароль.
     * В случае успеха выдается токен аутентификации. Если данные некорректны, пользователь получает
     * сообщение об ошибке.
     *
     * @param jwtRequest данные для аутентификации, такие как логин и пароль
     * @return объект с данными, содержащими токен аутентификации
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Аутентификация пользователя")
    public JwtResponse login(@Validated(Creatable.class) @RequestBody JwtRequest jwtRequest) {
        return securityService.login(jwtRequest);
    }

    /**
     * Выполняет регистрацию пользователя. Регистрацию может выполнять только владелец фермы.
     * При регистрации указываются персональные данные пользователя. В случае успешной регистрации
     * владелец получает информацию о новом зарегистрированном сотруднике. Если данные некорректны,
     * например, email занят, владелец получает сообщение об ошибке.
     *
     * @param userDto объект с данными пользователя для регистрации
     * @return объект с данными зарегистрированного пользователя
     */
    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация пользователя")
    public ResponseUserDto registration(@Validated(Creatable.class) @RequestBody UserDto userDto) {
        return securityService.registration(userDto);
    }
}


