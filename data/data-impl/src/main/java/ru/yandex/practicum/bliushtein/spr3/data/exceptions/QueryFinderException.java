package ru.yandex.practicum.bliushtein.spr3.data.exceptions;

public class QueryFinderException extends RuntimeException {

    private final static String EMPTY_PARAM_MESSAGE = "Parameter '%s' can't be empty";
    private final static String RESOURCE_NOT_FOUND_MESSAGE = "Query with key = '%s' and class  = '%s' is not found";

    public static QueryFinderException emptyParameter(String name) {
        return new QueryFinderException(EMPTY_PARAM_MESSAGE.formatted(name));
    }

    public static QueryFinderException resourceNotFound(String key, Class<?> aClass) {
        return new QueryFinderException(RESOURCE_NOT_FOUND_MESSAGE.formatted(key, aClass.getName()));
    }

    public QueryFinderException(String message) {
        super(message);
    }

    public QueryFinderException(String message, Throwable cause) {
        super(message, cause);
    }


}
