package ru.yandex.practicum.bliushtein.spr4.service;

import ru.yandex.practicum.bliushtein.spr4.service.dto.ImageOperation;

import java.util.UUID;

public interface UpdateImageService {

    UUID processImage(ImageOperation imageOperation);
}
