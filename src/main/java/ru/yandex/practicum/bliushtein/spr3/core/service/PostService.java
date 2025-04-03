package ru.yandex.practicum.bliushtein.spr3.core.service;

import ru.yandex.practicum.bliushtein.spr3.core.service.dto.PostDetails;
import ru.yandex.practicum.bliushtein.spr3.core.service.dto.PostSummary;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<PostSummary> findAll();

    List<PostSummary> findByTag(String tag);
    PostDetails getPostDetails(UUID id);

    void addLike(UUID id);

    void removeLike(UUID id);

    void deletePost(UUID id);

    void addComment(UUID postId, String text);

    void deleteComment(UUID commentId);

    void updateComment(UUID commentId, String text);

    UUID createPost(String name, String fullText, List<String> tags);

    void updatePost(UUID id, String name, String fullText, List<String> tags);
}
