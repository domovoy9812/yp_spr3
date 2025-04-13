package ru.yandex.practicum.bliushtein.spr3.data.repository;

import java.io.InputStream;
import java.util.UUID;

public interface FileStorage {
    UUID saveFile(InputStream file);
    void updateFile(UUID key, InputStream file);
    void deleteFile(UUID key);
    InputStream getFile(UUID key);
}
