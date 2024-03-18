package ru.relex.backend.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.relex.backend.exception.ApplicationError;
import ru.relex.backend.exception.ResourceIllegalStateException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс, перехватывающий и обрабатывающий все исключения в приложении.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение, возникающее при попытке войти
     * в приложение с неправильным логином или паролем.
     *
     * @param exception исключение {@link AuthenticationException}
     * @return объект с ошибкой для понятного предоставления пользователю
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApplicationError handleFailedAuthentication(AuthenticationException exception) {
        log.error("Error during authentication", exception);
        return ApplicationError.builder()
                .message("Аутентификация не удалась! Проверьте логин и пароль!")
                .build();
    }

    /**
     * Обрабатывает исключение, возникающее при попытке войти в приложение без достаточных
     * прав.
     *
     * @param exception исключение {@link AccessDeniedException}
     * @return объект с ошибкой для понятного предоставления пользователю
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApplicationError handleAccessDenied(AccessDeniedException exception) {
        log.error("Access denied error", exception);
        return ApplicationError.builder()
                .message("Доступ запрещён!")
                .build();
    }

    /**
     * Метод обрабатывает исключение, возникающее при некорректном состоянии ресурсов в приложении.
     * Некорректное состояние возникает при работе с ресурсами, работа с которыми не может быть корректной.
     * Возникает, например, при добавлении сотрудника с уже существующим идентификатором.
     *
     * @param exception исключение {@link ResourceIllegalStateException}
     * @return объект с ошибкой для понятного предоставления пользователю
     */
    @ExceptionHandler(ResourceIllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApplicationError handleIllegalState(ResourceIllegalStateException exception) {
        log.error("Error illegal state", exception);
        return ApplicationError.builder()
                .message(exception.getMessage())
                .build();
    }

    /**
     * Метод обрабатывает исключение, возникающее при передаче некорректных данных в контроллеры.
     *
     * @param exception исключение {@link MethodArgumentNotValidException}
     * @return объект с ошибкой для понятного предоставления пользователю
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApplicationError handleInvalidFormatDate(MethodArgumentNotValidException exception) {
        log.error("Validation failed", exception);
        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        return ApplicationError.builder()
                .message("Произошла ошибка при проверке введенных данных! Проверьте введенные данные и повторите попытку.")
                .errors(getCollectMapOfErrors(errors))
                .build();
    }


    /**
     * Метод обрабатывает исключение, возникающее при внутренней ошибке приложения.
     * Должно быть оперативно отслежено администратором сервиса.
     *
     * @param exception исключение {@link Exception}
     * @return объект с ошибкой для понятного предоставления пользователю
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApplicationError handleException(Exception exception) {
        log.error("Error", exception);
        return ApplicationError.builder()
                .message("Произошла внутренняя ошибка! Обратитесь к администратору сервиса!")
                .build();
    }

    private static Map<String, String> getCollectMapOfErrors(List<FieldError> errors) {
        return errors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage));
    }
}
