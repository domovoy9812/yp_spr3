package ru.yandex.practicum.bliushtein.spr3.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.bliushtein.spr3.model.Post;
import ru.yandex.practicum.bliushtein.spr3.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

}