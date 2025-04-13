package ru.yandex.practicum.bliushtein.spr3.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.bliushtein.spr3.data.repository.FileStorage;
import ru.yandex.practicum.bliushtein.spr3.service.ImageService;
import ru.yandex.practicum.bliushtein.spr3.service.dto.ImageOperation;
import ru.yandex.practicum.bliushtein.spr3.service.exceptions.ProcessImageException;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final FileStorage fileStorage;

    public ImageServiceImpl(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Override
    public InputStream getImageByKey(UUID key) {
        return fileStorage.getFile(key);
    }

    @Override
    public UUID processImage(ImageOperation imageOperation) {
        try {
            return switch (imageOperation.getAction()) {
                case ADD -> fileStorage.saveFile(imageOperation.getInputStream());
                case SAME -> imageOperation.getKey();
                case DELETE -> {
                    fileStorage.deleteFile(imageOperation.getKey());
                    yield null;
                }
                case UPDATE -> {
                    fileStorage.updateFile(imageOperation.getKey(), imageOperation.getInputStream());
                    yield imageOperation.getKey();
                }
            };
        } catch (IOException exception) {
            throw new ProcessImageException("Unable to process image file", exception);
        }
    }
}
