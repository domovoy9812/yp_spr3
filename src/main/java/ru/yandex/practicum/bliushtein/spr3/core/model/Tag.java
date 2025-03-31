package ru.yandex.practicum.bliushtein.spr3.core.model;

import java.util.UUID;

public class Tag {
    private UUID postId;
    private String name;

    public Tag(UUID postId, String name) {
        this.postId = postId;
        this.name = name;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
