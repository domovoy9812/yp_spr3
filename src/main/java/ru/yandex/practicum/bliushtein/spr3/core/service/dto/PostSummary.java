package ru.yandex.practicum.bliushtein.spr3.core.service.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class PostSummary {
    protected UUID id;
    protected String name;
    protected String shortText;
    protected ZonedDateTime createdWhen;
    protected int likesCount;
    protected List<String> tags;
    protected int commentsCount;

    public PostSummary(UUID id, String name, String shortText, ZonedDateTime createdWhen, int likesCount,
                       List<String> tags, int commentsCount) {
        this.id = id;
        this.name = name;
        this.shortText = shortText;
        this.createdWhen = createdWhen;
        this.likesCount = likesCount;
        this.tags = tags;
        this.commentsCount = commentsCount;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
