package ru.yandex.practicum.bliushtein.spr3.service.exceptions;

public class ProcessImageException extends RuntimeException {

    public ProcessImageException(String message) {
        super(message);
    }

    public ProcessImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
