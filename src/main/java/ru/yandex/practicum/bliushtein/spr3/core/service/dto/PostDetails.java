package ru.yandex.practicum.bliushtein.spr3.core.service.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class PostDetails extends PostSummary {

    private String fullText;
    private List<CommentInfo> comments;

    public PostDetails(UUID id, String name, String shortText, String fullText, ZonedDateTime createdWhen,
                       int likesCount, List<String> tags, List<CommentInfo> comments) {
        super(id, name, shortText, createdWhen, likesCount, tags, comments.size());
        this.fullText = fullText;
        this.comments = comments;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public List<CommentInfo> getComments() {
        return comments;
    }

    public void setComments(List<CommentInfo> comments) {
        this.comments = comments;
    }
}
