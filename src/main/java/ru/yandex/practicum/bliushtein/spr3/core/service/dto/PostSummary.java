package ru.yandex.practicum.bliushtein.spr3.core.service.dto;

import ru.yandex.practicum.bliushtein.spr3.core.model.Post;
import ru.yandex.practicum.bliushtein.spr3.core.model.Tag;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostSummary {
    private String id;
    private String name;
    private String shortText;
    private ZonedDateTime createdWhen;
    private int likesCount;
    private List<String> tags;
    private int commentsCount;

    public PostSummary(Post post, List<Tag> tags, int commentsCount) {
        this(post.getId().toString(), post.getName(), post.getShortText(), post.getCreatedWhen(),
                post.getLikesCount(), tags.stream().map(Tag::getName).collect(Collectors.toList()), commentsCount);
    }

    public PostSummary(String id, String name, String shortText, ZonedDateTime createdWhen, int likesCount,
                       List<String> tags, int commentsCount) {
        this.id = id;
        this.name = name;
        this.shortText = shortText;
        this.createdWhen = createdWhen;
        this.likesCount = likesCount;
        this.tags = tags;
        this.commentsCount = commentsCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
