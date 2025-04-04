package ru.yandex.practicum.bliushtein.spr3.service.dto;

import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PostDetails extends PostSummary {

    private String fullText;
    private List<CommentInfo> comments;

    public PostDetails() {
        super(null, StringUtils.EMPTY, StringUtils.EMPTY, null, 0, Collections.emptyList(),
                0, null);
        this.fullText = StringUtils.EMPTY;
        this.comments = Collections.emptyList();
    }

    public PostDetails(UUID id, String name, String shortText, String fullText, ZonedDateTime createdWhen,
                       int likesCount, List<String> tags, List<CommentInfo> comments, UUID imageKey) {
        super(id, name, shortText, createdWhen, likesCount, tags, comments.size(), imageKey);
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
