package ru.yandex.practicum.bliushtein.spr3.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Post {
    private UUID id;
    private String name;
    private String text;
    private ZonedDateTime createdWhen;
    private int likesCount;

    public Post(UUID id, String name, String text, ZonedDateTime createdWhen, int likesCount) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.createdWhen = createdWhen;
        this.likesCount = likesCount;
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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
}
