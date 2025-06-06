package ru.yandex.practicum.bliushtein.spr3.service.dto;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface ImageOperation {
    enum Action {
        ADD,
        UPDATE,
        DELETE,
        SAME
    }
    InputStream getInputStream() throws IOException;
    UUID getKey();
    Action getAction();
}
