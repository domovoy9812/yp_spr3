package ru.yandex.practicum.bliushtein.spr4.service.exceptions;

public class ProcessImageException extends RuntimeException {

    public ProcessImageException(String message) {
        super(message);
    }

    public ProcessImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
