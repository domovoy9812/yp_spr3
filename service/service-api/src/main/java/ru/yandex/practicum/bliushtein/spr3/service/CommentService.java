package ru.yandex.practicum.bliushtein.spr3.service;

import java.util.UUID;

public interface CommentService {
    void addComment(UUID postId, String text);

    void deleteComment(UUID commentId);

    void updateComment(UUID commentId, String text);
}
