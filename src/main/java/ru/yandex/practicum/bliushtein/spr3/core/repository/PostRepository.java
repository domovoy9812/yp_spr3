package ru.yandex.practicum.bliushtein.spr3.core.repository;

import ru.yandex.practicum.bliushtein.spr3.core.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.core.model.Post;
import ru.yandex.practicum.bliushtein.spr3.core.model.Tag;

import java.util.List;
import java.util.UUID;

public interface PostRepository {
    List<Post> getAllPosts();
    List<Tag> getTags(UUID postId);
    int getCommentsCount(UUID postId);
    List<Post> getPostByTag(String tag);
    List<Comment> getComments(UUID postId);

    Post getPost(UUID id);

    void addLike(UUID id);

    void removeLike(UUID id);

    void deletePost(UUID id);
}
