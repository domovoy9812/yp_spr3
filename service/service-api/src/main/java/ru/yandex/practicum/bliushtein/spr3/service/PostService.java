package ru.yandex.practicum.bliushtein.spr3.service;

import ru.yandex.practicum.bliushtein.spr3.service.dto.ImageInfo;
import ru.yandex.practicum.bliushtein.spr3.service.dto.PostDetails;
import ru.yandex.practicum.bliushtein.spr3.service.dto.PostSummary;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface PostService {
    List<PostSummary> findAll();

    List<PostSummary> findByTag(String tag);
    PostDetails getPostDetails(UUID id);

    void addLike(UUID id);

    void removeLike(UUID id);

    void deletePost(UUID id, ImageInfo imageInfo);

    void addComment(UUID postId, String text);

    void deleteComment(UUID commentId);

    void updateComment(UUID commentId, String text);

    UUID createPost(String name, String fullText, List<String> tags, ImageInfo imageInfo);

    void updatePost(UUID id, String name, String fullText, List<String> tags, ImageInfo imageInfo);

    InputStream getImageByKey(UUID key);
}
