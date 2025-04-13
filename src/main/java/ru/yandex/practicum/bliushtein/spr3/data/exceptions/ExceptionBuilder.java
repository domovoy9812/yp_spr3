package ru.yandex.practicum.bliushtein.spr3.data.exceptions;

import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.bliushtein.spr3.data.repository.DataAccessException;

import java.util.*;
import java.util.stream.Collectors;

public class ExceptionBuilder {
    private static final String GENERIC_ERROR = "Error during";
    private static final String POST_IS_NOT_FOUND = "Post is not found";
    private static final String EMPTY_INPUT_PARAMS = "Not all input parameters filled for";
    private static final String INCORRECT_INPUT_PARAMS = "Incorrect input parameters for";

    private final String messagePrefix;
    private final Exception cause;
    private final String operation;
    Map<String, String> params;

    private ExceptionBuilder(String messagePrefix, String operation, Exception cause) {
        this.params = new LinkedHashMap<>();
        this.messagePrefix = messagePrefix;
        this.operation = operation;
        this.cause = cause;
    }

    private ExceptionBuilder(String messagePrefix, String operation) {
        this(messagePrefix, operation, null);
    }

    public static ExceptionBuilder postIsNotFound(UUID id, Exception exception) {
        return new ExceptionBuilder(POST_IS_NOT_FOUND, null, exception).withPostId(id);
    }

    public static ExceptionBuilder postIsNotFound(UUID id) {
        return new ExceptionBuilder(POST_IS_NOT_FOUND, null, null).withPostId(id);
    }

    public static ExceptionBuilder emptyInputParameters(String operation) {
        return new ExceptionBuilder(EMPTY_INPUT_PARAMS, operation, null);
    }

    public static ExceptionBuilder incorrectInputParameters(String operation) {
        return new ExceptionBuilder(INCORRECT_INPUT_PARAMS, operation, null);
    }

    public static ExceptionBuilder of(String operation, Exception exception) {
        return new ExceptionBuilder(GENERIC_ERROR, operation, exception);
    }

    public static ExceptionBuilder of(String messagePrefix, String operation, Exception exception) {
        return new ExceptionBuilder(messagePrefix, operation, exception);
    }

    public static ExceptionBuilder of(String operation) {
        return new ExceptionBuilder(GENERIC_ERROR, operation);
    }

    public static ExceptionBuilder of(String messagePrefix, String operation) {
        return new ExceptionBuilder(messagePrefix, operation);
    }

    public DataAccessException build() {
        StringBuilder messageBuilder = new StringBuilder(messagePrefix);
        if (StringUtils.isNotBlank(operation)) {
            messageBuilder.append(" '").append(operation).append('\'');
        }
        messageBuilder.append(" params {");
        params.forEach((name, value) -> messageBuilder.append('\'').append(name).append("'='")
                .append(value).append(('\'')));
        messageBuilder.append('}');
        return new DataAccessException(messageBuilder.toString(), cause, operation, params);
    }

    public ExceptionBuilder withPostId(UUID id) {
        params.put("post id", safeToString(id));
        return this;
    }

    public ExceptionBuilder withCommentId(UUID id) {
        params.put("comment id", safeToString(id));
        return this;
    }

    public ExceptionBuilder withComment(String text) {
        params.put("comment text", safeToString(text));
        return this;
    }

    public ExceptionBuilder withTag(String tag) {
        params.put("tag", safeToString(tag));
        return this;
    }

    public ExceptionBuilder withPostName(String name) {
        params.put("post name", safeToString(name));
        return this;
    }

    public ExceptionBuilder withFullText(String text) {
        params.put("full text", safeToString(text));
        return this;
    }

    public ExceptionBuilder withShortText(String text) {
        params.put("short text", safeToString(text));
        return this;
    }

    public ExceptionBuilder withImageId(UUID id) {
        params.put("image id", safeToString(id));
        return this;
    }

    public ExceptionBuilder withTags(Collection<String> removedTags) {
        params.put("tags", convertCollectionToString(removedTags));
        return this;
    }

    private String convertCollectionToString(Collection<String> strings) {
        return strings.stream().map(s -> s == null ? StringUtils.EMPTY : s)
                .collect(Collectors.joining(",", "[", "]"));
    }

    private String safeToString(Object value) {
        return Objects.toString(value, StringUtils.EMPTY);
    }

    public ExceptionBuilder withEmptyFile() {
        params.put("file", StringUtils.EMPTY);
        return this;
    }
}
