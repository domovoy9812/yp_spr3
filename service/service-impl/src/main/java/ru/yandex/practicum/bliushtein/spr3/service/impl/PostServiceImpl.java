package ru.yandex.practicum.bliushtein.spr3.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.bliushtein.spr3.data.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.data.model.Post;
import ru.yandex.practicum.bliushtein.spr3.data.repository.FileStorage;
import ru.yandex.practicum.bliushtein.spr3.data.repository.PostRepository;
import ru.yandex.practicum.bliushtein.spr3.service.PostService;
import ru.yandex.practicum.bliushtein.spr3.service.dto.CommentInfo;
import ru.yandex.practicum.bliushtein.spr3.service.dto.ImageInfo;
import ru.yandex.practicum.bliushtein.spr3.service.dto.PostDetails;
import ru.yandex.practicum.bliushtein.spr3.service.dto.PostSummary;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FileStorage fileStorage;
    public PostServiceImpl(PostRepository postRepository, FileStorage fileStorage) {
        this.postRepository = postRepository;
        this.fileStorage = fileStorage;
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
                post.getLikesCount(), tags, commentsCount, post.getImageKey());
    }

    @Override
    public List<PostSummary> findByTag(String tag) {
        List<Post> posts = postRepository.getPostByTag(tag);
        return posts.stream().map(this::createPostSummary).collect(Collectors.toList());
    }

    @Override
    public PostDetails getPostDetails(UUID id) {
        Post post = postRepository.getPost(id);
        List<String> tags = postRepository.getTags(id);
        List<Comment> comments = postRepository.getComments(id);
        return new PostDetails(post.getId(), post.getName(), post.getShortText(), post.getFullText(),
                post.getCreatedWhen(), post.getLikesCount(), tags,
                transformCommentsToCommentInfos(comments), post.getImageKey());
    }

    private List<CommentInfo> transformCommentsToCommentInfos(List<Comment> comments) {
        return comments.stream()
                .map(c -> new CommentInfo(c.getId(), c.getText(), c.getCreatedWhen()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UUID createPost(String name, String fullText, List<String> tags, ImageInfo imageInfo) {
        UUID imageKey = processImage(imageInfo);
        UUID id = postRepository.createPost(name, fullText, getShortText(fullText), imageKey);
        createTags(id, tags);
        return id;
    }

    private UUID processImage(ImageInfo imageInfo) {
        try {
            return switch (imageInfo.getAction()) {
                case ADD -> fileStorage.saveFile(imageInfo.getInputStream());
                case SAME -> imageInfo.getKey();
                case DELETE -> {
                    fileStorage.deleteFile(imageInfo.getKey());
                    yield null;
                }
                case UPDATE -> {
                    fileStorage.updateFile(imageInfo.getKey(), imageInfo.getInputStream());
                    yield imageInfo.getKey();
                }
            };
        } catch (IOException exception) {
            //TODO add correct exception class
            throw new RuntimeException("Unable to process image file");
        }
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
    public void updatePost(UUID id, String name, String fullText, List<String> tags, ImageInfo imageInfo) {
        UUID imageKey = processImage(imageInfo);
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
    public void deletePost(UUID id, ImageInfo imageInfo) {
        processImage(imageInfo);
        postRepository.deletePost(id);
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

    @Override
    public InputStream getImageByKey(UUID key) {
        return fileStorage.getFileByKey(key);
    }
}