package ru.yandex.practicum.bliushtein.spr3.service.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public class CommentInfo {
    UUID id;
    String text;
    ZonedDateTime createdWhen;

    public CommentInfo(UUID id, String text, ZonedDateTime createdWhen) {
        this.id = id;
        this.text = text;
        this.createdWhen = createdWhen;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getCreatedWhen() {
        return createdWhen;
    }

    public void setCreatedWhen(ZonedDateTime createdWhen) {
        this.createdWhen = createdWhen;
    }
}
