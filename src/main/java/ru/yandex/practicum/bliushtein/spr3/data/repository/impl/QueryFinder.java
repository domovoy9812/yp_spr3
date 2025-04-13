package ru.yandex.practicum.bliushtein.spr3.data.repository.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.bliushtein.spr3.data.exceptions.QueryFinderException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class QueryFinder {

    private final static String RESOURCE_NAME_TEMPLATE = "classpath:%s/%s.sql";

    private final ResourceLoader resourceLoader;

    public QueryFinder(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String findQuery(String key, Class<?> aClass) {
        if (key == null) {
            throw QueryFinderException.emptyParameter("key");
        }
        if (aClass == null) {
            throw QueryFinderException.emptyParameter("aClass");
        }
        try {
            Resource resource = resourceLoader.getResource(
                    RESOURCE_NAME_TEMPLATE.formatted(aClass.getName().replace('.', '/'), key));
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new QueryFinderException("Error during query read", e);
        }
    }
}
