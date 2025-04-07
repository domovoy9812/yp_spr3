package ru.yandex.practicum.bliushtein.spr3.service;

import ru.yandex.practicum.bliushtein.spr3.service.dto.ImageOperation;

import java.util.UUID;

public interface UpdateImageService {

    UUID processImage(ImageOperation imageOperation);
}
