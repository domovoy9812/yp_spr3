package ru.yandex.practicum.bliushtein.spr3.service;

import java.io.InputStream;
import java.util.UUID;

public interface GetImageService {
    InputStream getImageByKey(UUID key);

}
