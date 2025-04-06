package ru.yandex.practicum.bliushtein.spr3.service;

import ru.yandex.practicum.bliushtein.spr3.service.dto.ImageOperation;

import java.io.InputStream;
import java.util.UUID;

public interface ImageService {
    InputStream getImageByKey(UUID key);

    UUID processImage(ImageOperation imageOperation);
}
