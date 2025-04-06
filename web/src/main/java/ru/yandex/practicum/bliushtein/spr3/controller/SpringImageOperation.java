package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.bliushtein.spr3.service.dto.ImageOperation;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class SpringImageOperation implements ImageOperation {

    private static final SpringImageOperation SAME_IMAGE_INFO = new SpringImageOperation(null, null, Action.SAME);

    public static SpringImageOperation forDelete(UUID imageKey) {
        if (imageKey != null) {
            return new SpringImageOperation(null, imageKey, Action.DELETE);
        } else {
            return SAME_IMAGE_INFO;
        }
    }

    public static SpringImageOperation forCreate(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return SAME_IMAGE_INFO;
        } else {
            return new SpringImageOperation(image, null, Action.ADD);
        }
    }

    public static SpringImageOperation forUpdate(MultipartFile image, UUID imageKey) {
        if (image == null || image.isEmpty()) {
            if (imageKey == null) {
                return SAME_IMAGE_INFO;
            } else {
                return new SpringImageOperation(null, imageKey, Action.SAME);
            }
        } else {
            return new SpringImageOperation(image, imageKey, imageKey == null ? Action.ADD : Action.UPDATE);
        }
    }

    final private MultipartFile image;
    final private UUID imageKey;
    final Action action;

    private SpringImageOperation(MultipartFile image, UUID imageKey, Action action) {
        this.image = image;
        this.imageKey = imageKey;
        this.action = action;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.image == null ? null : image.getInputStream();
    }

    @Override
    public UUID getKey() {
        return this.imageKey;
    }

    @Override
    public Action getAction() {
        return this.action;
    }
}
