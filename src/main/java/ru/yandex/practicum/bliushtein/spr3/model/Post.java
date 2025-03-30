package ru.yandex.practicum.bliushtein.spr3.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Post {
    private UUID id;
    private String name;
    private String text;
    private ZonedDateTime createdWhen;
    private UUID owner;

    public Post(UUID id, String name, String text, ZonedDateTime createdWhen, UUID owner) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.createdWhen = createdWhen;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public ZonedDateTime getCreatedWhen() {
        return createdWhen;
    }

    public UUID getOwner() {
        return owner;
    }
}
