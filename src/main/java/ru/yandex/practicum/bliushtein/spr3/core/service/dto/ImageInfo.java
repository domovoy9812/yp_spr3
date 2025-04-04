package ru.yandex.practicum.bliushtein.spr3.core.service.dto;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface ImageInfo {
    enum Action {
        ADD,
        UPDATE,
        DELETE,
        SAME
    }
    long getLength();
    InputStream getInputStream() throws IOException;
    UUID getKey();
    Action getAction();
}
