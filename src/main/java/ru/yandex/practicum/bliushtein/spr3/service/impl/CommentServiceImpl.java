package ru.yandex.practicum.bliushtein.spr3.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.bliushtein.spr3.data.repository.PostRepository;
import ru.yandex.practicum.bliushtein.spr3.service.CommentService;

import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;

    public CommentServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void addComment(UUID postId, String text) {
        postRepository.addComment(postId, text);
    }

    @Override
    public void deleteComment(UUID commentId) {
        postRepository.deleteComment(commentId);
    }

    @Override
    public void updateComment(UUID commentId, String text) {
        postRepository.updateComment(commentId, text);
    }

}
