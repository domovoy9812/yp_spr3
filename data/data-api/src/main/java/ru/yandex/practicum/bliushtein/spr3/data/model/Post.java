package ru.yandex.practicum.bliushtein.spr3.data.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Post {
    private UUID id;
    private String name;
    private String fullText;
    private String shortText;
    private ZonedDateTime createdWhen;
    private int likesCount;
    private UUID imageKey;

    public Post(UUID id, String name, String fullText, String shortText, ZonedDateTime createdWhen, int likesCount,
                UUID imageKey) {
        this.id = id;
        this.name = name;
        this.fullText = fullText;
        this.shortText = shortText;
        this.createdWhen = createdWhen;
        this.likesCount = likesCount;
        this.imageKey = imageKey;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public ZonedDateTime getCreatedWhen() {
        return createdWhen;
    }

    public void setCreatedWhen(ZonedDateTime createdWhen) {
        this.createdWhen = createdWhen;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public UUID getImageKey() {
        return imageKey;
    }

    public void setImageKey(UUID imageKey) {
        this.imageKey = imageKey;
    }
}
