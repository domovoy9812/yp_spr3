package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.bliushtein.spr3.service.dto.ImageInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class SpringImageInfo implements ImageInfo {

    private static final SpringImageInfo SAME_IMAGE_INFO = new SpringImageInfo(null, null, Action.SAME);

    public static SpringImageInfo forDelete(UUID imageKey) {
        if (imageKey != null) {
            return new SpringImageInfo(null, imageKey, Action.DELETE);
        } else {
            return SAME_IMAGE_INFO;
        }
    }

    public static SpringImageInfo forCreate(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return SAME_IMAGE_INFO;
        } else {
            return new SpringImageInfo(image, null, Action.ADD);
        }
    }

    public static SpringImageInfo forUpdate(MultipartFile image, UUID imageKey) {
        if (image == null || image.isEmpty()) {
            if (imageKey == null) {
                return SAME_IMAGE_INFO;
            } else {
                return new SpringImageInfo(null, imageKey, Action.SAME);
            }
        } else {
            return new SpringImageInfo(image, imageKey, imageKey == null ? Action.ADD : Action.UPDATE);
        }
    }

    final private MultipartFile image;
    final private UUID imageKey;
    final Action action;

    private SpringImageInfo(MultipartFile image, UUID imageKey, Action action) {
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
