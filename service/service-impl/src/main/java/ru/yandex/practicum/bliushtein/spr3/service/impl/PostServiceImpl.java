package ru.yandex.practicum.bliushtein.spr3.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.bliushtein.spr3.data.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.data.model.Post;
import ru.yandex.practicum.bliushtein.spr3.data.repository.PostRepository;
import ru.yandex.practicum.bliushtein.spr3.service.ImageService;
import ru.yandex.practicum.bliushtein.spr3.service.PostService;
import ru.yandex.practicum.bliushtein.spr3.service.dto.CommentInfo;
import ru.yandex.practicum.bliushtein.spr3.service.dto.ImageOperation;
import ru.yandex.practicum.bliushtein.spr3.service.dto.PostDetails;
import ru.yandex.practicum.bliushtein.spr3.service.dto.PostSummary;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageService imageService;

    public PostServiceImpl(PostRepository postRepository, ImageService imageService) {
        this.postRepository = postRepository;
        this.imageService = imageService;
    }

    @Override
    public List<PostSummary> findAll() {
        List<Post> posts = postRepository.getAllPosts();
        return posts.stream().map(this::createPostSummary).collect(Collectors.toList());
    }

    private PostSummary createPostSummary(Post post) {
        List<String> tags = postRepository.getTags(post.getId());
        int commentsCount = postRepository.getCommentsCount(post.getId());
        return new PostSummary(post.getId(), post.getName(), post.getShortText(), post.getCreatedWhen(),
                post.getLikesCount(), tags, commentsCount, post.getImageId());
    }

    @Override
    public List<PostSummary> findByTag(String tag) {
        List<Post> posts = postRepository.getPostsByTag(tag);
        return posts.stream().map(this::createPostSummary).collect(Collectors.toList());
    }

    @Override
    public PostDetails getPostDetails(UUID id) {
        Post post = postRepository.getPost(id);
        List<String> tags = postRepository.getTags(id);
        List<Comment> comments = postRepository.getComments(id);
        return new PostDetails(post.getId(), post.getName(), post.getShortText(), post.getFullText(),
                post.getCreatedWhen(), post.getLikesCount(), tags,
                transformCommentsToCommentInfos(comments), post.getImageId());
    }

    private List<CommentInfo> transformCommentsToCommentInfos(List<Comment> comments) {
        return comments.stream()
                .map(c -> new CommentInfo(c.getId(), c.getText(), c.getCreatedWhen()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UUID createPost(String name, String fullText, List<String> tags, ImageOperation imageOperation) {
        UUID imageKey = imageService.processImage(imageOperation);
        UUID id = postRepository.createPost(name, fullText, getShortText(fullText), imageKey);
        createTags(id, tags);
        return id;
    }

    private static String getShortText(String fullText) {
        return fullText.lines().findFirst().orElse(StringUtils.EMPTY);
    }

    private void createTags(UUID id, Collection<String> tags) {
        for (String tag : tags) {
            postRepository.createTag(id, tag);
        }
    }

    private void removeTags(UUID id, Collection<String> removedTags) {
        if (CollectionUtils.isNotEmpty(removedTags)) {
            postRepository.deleteTags(id, removedTags);
        }
    }

    @Override
    @Transactional
    public void updatePost(UUID id, String name, String fullText, List<String> tags, ImageOperation imageOperation) {
        UUID imageKey = imageService.processImage(imageOperation);
        postRepository.updatePost(id, name, fullText, getShortText(fullText), imageKey);
        List<String> storedTags = postRepository.getTags(id);
        Collection<String> addedTags = CollectionUtils.subtract(tags, storedTags);
        createTags(id, addedTags);
        Collection<String> removedTags = CollectionUtils.subtract(storedTags, tags);
        removeTags(id, removedTags);
    }

    @Override
    public void addLike(UUID id) {
        postRepository.addLike(id);
    }

    @Override
    public void removeLike(UUID id) {
        postRepository.removeLike(id);
    }

    @Override
    @Transactional
    public void deletePost(UUID id, ImageOperation imageOperation) {
        imageService.processImage(imageOperation);
        postRepository.deletePost(id);
    }


}