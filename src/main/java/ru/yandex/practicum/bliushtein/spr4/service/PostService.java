package ru.yandex.practicum.bliushtein.spr4.service;

import ru.yandex.practicum.bliushtein.spr4.service.dto.ImageOperation;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostDetails;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostSummary;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<PostSummary> findAll();

    List<PostSummary> findByTag(String tag);
    PostDetails getPostDetails(UUID id);

    void addLike(UUID id);

    void removeLike(UUID id);

    void deletePost(UUID id, ImageOperation imageOperation);

    UUID createPost(String name, String fullText, List<String> tags, ImageOperation imageOperation);

    void updatePost(UUID id, String name, String fullText, List<String> tags, ImageOperation imageOperation);

}
