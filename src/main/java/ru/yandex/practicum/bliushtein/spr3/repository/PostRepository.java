package ru.yandex.practicum.bliushtein.spr3.repository;

import ru.yandex.practicum.bliushtein.spr3.model.Post;
import java.util.List;

public interface PostRepository {
    List<Post> findAll();
}
