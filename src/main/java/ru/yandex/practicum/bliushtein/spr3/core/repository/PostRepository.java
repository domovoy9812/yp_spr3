package ru.yandex.practicum.bliushtein.spr3.core.repository;

import ru.yandex.practicum.bliushtein.spr3.core.model.Post;
import ru.yandex.practicum.bliushtein.spr3.core.model.Tag;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();
    List<Tag> getTags(Post post);
    int getCommentsCount(Post post);
}
