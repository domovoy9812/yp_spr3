package ru.yandex.practicum.bliushtein.spr4.service;

import java.io.InputStream;
import java.util.UUID;

public interface GetImageService {
    InputStream getImageByKey(UUID key);

}
