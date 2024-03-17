package ru.relex.backend.exception;

/**
 * Исключение, возникающее при не регламентированной работе с ресурсом в приложении.
 * Например, при попытке удалить ресурс, который не был создан.
 */
public class ResourceIllegalStateException extends RuntimeException{
    public ResourceIllegalStateException(String message) {
        super(message);
    }
}
