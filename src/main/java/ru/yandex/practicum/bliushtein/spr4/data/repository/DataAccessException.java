package ru.yandex.practicum.bliushtein.spr4.data.repository;

import java.util.Collections;
import java.util.Map;

public class DataAccessException extends RuntimeException {

    private final String operation;
    private final Map<String, String> parameters;

    public DataAccessException(String message, Throwable cause, String operation, Map<String, String> parameters) {
        super(message, cause);
        this.operation = operation;
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    public String getOperation() {
        return operation;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
