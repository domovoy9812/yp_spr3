package ru.yandex.practicum.bliushtein.spr3.core.service;

import ru.yandex.practicum.bliushtein.spr3.core.service.dto.PostSummary;

import java.util.List;

public interface PostService {
    List<PostSummary> findAll();
}
