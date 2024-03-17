package ru.relex.backend.exception;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

/**
 * Класс ошибки приложения. Содержит информацию об ошибках, которые произошли в процессе
 * работы. Поле {@link #errors } необходимо для случаев, когда произошло несколько ошибок,
 * но все логически они относятся к одной группе, в основном при валидации. В качестве
 * ключа используется название валидируемого поля, а в качестве значения - сообщение об ошибке.
 */
@Value
@Builder
public class ApplicationError {
    String message;
    Map<String, String> errors;
}
