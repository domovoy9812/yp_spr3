package ru.yandex.practicum.bliushtein.spr3.data.repository;

import ru.yandex.practicum.bliushtein.spr3.data.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.data.model.Post;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PostRepository {
    List<Post> getAllPosts();
    List<String> getTags(UUID postId);
    int getCommentsCount(UUID postId);
    List<Post> getPostByTag(String tag);
    List<Comment> getComments(UUID postId);

    Post getPost(UUID id);

    void addLike(UUID id);

    void removeLike(UUID id);

    void deletePost(UUID id);

    void addComment(UUID postId, String text);

    void deleteComment(UUID commentId);

    void updateComment(UUID commentId, String text);

    UUID createPost(String name, String fullText, String shortText, UUID imageKey);

    void updatePost(UUID id, String name, String fullText, String shortText, UUID imageKey);

    void createTag(UUID id, String tag);

    void deleteTags(UUID id, Collection<String> removedTags);

}
