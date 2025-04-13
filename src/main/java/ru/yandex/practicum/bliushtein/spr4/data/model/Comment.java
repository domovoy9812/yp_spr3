package ru.yandex.practicum.bliushtein.spr4.data.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Comment {
    UUID id;
    String text;
    ZonedDateTime createdWhen;
    UUID postId;

    public Comment(UUID id, String text, ZonedDateTime createdWhen, UUID postId) {
        this.id = id;
        this.text = text;
        this.createdWhen = createdWhen;
        this.postId = postId;
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

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }
}
