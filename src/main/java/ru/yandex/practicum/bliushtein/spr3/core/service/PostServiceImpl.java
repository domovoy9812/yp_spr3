package ru.yandex.practicum.bliushtein.spr3.core.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.bliushtein.spr3.core.model.Post;
import ru.yandex.practicum.bliushtein.spr3.core.model.Tag;
import ru.yandex.practicum.bliushtein.spr3.core.repository.PostRepository;
import ru.yandex.practicum.bliushtein.spr3.core.service.dto.PostSummary;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<PostSummary> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::createPostSummary).collect(Collectors.toList());
    }

    private PostSummary createPostSummary(Post post) {
        List<Tag> tags = postRepository.getTags(post);
        int commentsCount = postRepository.getCommentsCount(post);
        return new PostSummary(post, tags, commentsCount);
    }
}