package ru.yandex.practicum.bliushtein.spr3.core.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.bliushtein.spr3.core.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.core.model.Post;
import ru.yandex.practicum.bliushtein.spr3.core.model.Tag;
import ru.yandex.practicum.bliushtein.spr3.core.repository.PostRepository;
import ru.yandex.practicum.bliushtein.spr3.core.service.dto.CommentInfo;
import ru.yandex.practicum.bliushtein.spr3.core.service.dto.PostDetails;
import ru.yandex.practicum.bliushtein.spr3.core.service.dto.PostSummary;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<PostSummary> findAll() {
        List<Post> posts = postRepository.getAllPosts();
        return posts.stream().map(this::createPostSummary).collect(Collectors.toList());
    }

    private PostSummary createPostSummary(Post post) {
        List<Tag> tags = postRepository.getTags(post.getId());
        int commentsCount = postRepository.getCommentsCount(post.getId());
        return new PostSummary(post.getId(), post.getName(), post.getShortText(), post.getCreatedWhen(),
                post.getLikesCount(), transformTagsToStrings(tags), commentsCount);
    }

    private List<String> transformTagsToStrings(List<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toList());
    }

    @Override
    public List<PostSummary> findByTag(String tag) {
        List<Post> posts = postRepository.getPostByTag(tag);
        return posts.stream().map(this::createPostSummary).collect(Collectors.toList());
    }

    @Override
    public PostDetails getPostDetails(UUID id) {
        Post post = postRepository.getPost(id);
        List<Tag> tags = postRepository.getTags(id);
        List<Comment> comments = postRepository.getComments(id);
        return new PostDetails(post.getId(), post.getName(), post.getShortText(), post.getFullText(),
                post.getCreatedWhen(), post.getLikesCount(), transformTagsToStrings(tags),
                transformCommentsToCommentInfos(comments));
    }

    private List<CommentInfo> transformCommentsToCommentInfos(List<Comment> comments) {
        return comments.stream()
                .map(c -> new CommentInfo(c.getId(), c.getText(), c.getCreatedWhen()))
                .collect(Collectors.toList());
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
    public void deletePost(UUID id) {
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
}