package ru.yandex.practicum.bliushtein.spr3.data.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.bliushtein.spr3.data.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.data.model.Post;
import ru.yandex.practicum.bliushtein.spr3.data.exceptions.ExceptionBuilder;
import ru.yandex.practicum.bliushtein.spr3.data.repository.PostRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private static final String GET_ALL_POSTS_QUERY_NAME = "get_all_posts";
    private static final String GET_POST_BY_ID_QUERY_NAME = "get_post_by_id";
    private static final String GET_TAGS_BY_POST_ID_QUERY_NAME = "get_tags_by_post_id";
    private static final String GET_COMMENTS_COUNT_FOR_POST_QUERY_NAME = "get_comments_count_for_post";
    private static final String GET_POSTS_BY_TAG_QUERY_NAME = "get_posts_by_tag";
    private static final String GET_COMMENTS_BY_POST_ID_QUERY_NAME = "get_comments_by_post_id";
    private static final String INCREMENT_LIKES_QUERY_NAME = "increment_likes";
    private static final String DECREMENT_LIKES_QUERY_NAME = "decrement_likes";
    private static final String DELETE_POST_QUERY_NAME = "delete_post";
    private static final String ADD_COMMENT_QUERY_NAME = "add_comment";
    private static final String DELETE_COMMENT_QUERY_NAME = "delete_comment";
    private static final String UPDATE_COMMENT_QUERY_NAME = "update_comment";
    private static final String ADD_POST_QUERY_NAME = "add_post";
    private static final String UPDATE_POST_QUERY_NAME = "update_post";
    private static final String ADD_TAG_QUERY_NAME = "add_tag";
    private static final String DELETE_TAG_QUERY_NAME = "delete_tag";
    private static final String CHECK_POST_EXISTS_QUERY_NAME = "check_post_exists";

    private static final String VARCHAR_TYPE = "varchar";

    private final JdbcTemplate jdbcTemplate;
    private final QueryFinder queryFinder;
    private final RowMapper<Post> postMapper;
    private final RowMapper<String> tagMapper;
    private final RowMapper<Comment> commentMapper;
    private final RowMapper<UUID> idMapper;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate, QueryFinder queryFinder,
                                    @Qualifier("postMapper") RowMapper<Post> postMapper,
                                    @Qualifier("tagMapper") RowMapper<String> tagMapper,
                                    @Qualifier("commentMapper") RowMapper<Comment> commentMapper,
                                    @Qualifier("idMapper") RowMapper<UUID> idMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryFinder = queryFinder;
        this.postMapper = postMapper;
        this.tagMapper = tagMapper;
        this.commentMapper = commentMapper;
        this.idMapper = idMapper;
    }

    @Override
    public List<Post> getAllPosts() {
        try {
            return jdbcTemplate.query(
                    queryFinder.findQuery(GET_ALL_POSTS_QUERY_NAME, JdbcNativePostRepository.class),
                    postMapper);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("get all posts", exception).build();
        }
    }

    @Override
    public Post getPost(UUID id) {
        try {
            return jdbcTemplate.queryForObject(
                    queryFinder.findQuery(GET_POST_BY_ID_QUERY_NAME, JdbcNativePostRepository.class),
                    postMapper, id);
        } catch (EmptyResultDataAccessException exception) {
            throw ExceptionBuilder.postIsNotFound(id, exception).build();
        }
        catch (Exception exception) {
            throw ExceptionBuilder.of("get post", exception).withPostId(id).build();
        }
    }

    @Override
    public List<String> getTags(UUID postId) {
        checkPostExistence(postId);
        try {
            return jdbcTemplate.queryForStream(
                    queryFinder.findQuery(GET_TAGS_BY_POST_ID_QUERY_NAME, JdbcNativePostRepository.class),
                    tagMapper,
                    postId).collect(Collectors.toList());
        } catch (Exception exception) {
            throw ExceptionBuilder.of("get tags", exception).withPostId(postId).build();
        }
    }

    @Override
    public int getCommentsCount(UUID postId) {
        checkPostExistence(postId);
        try {
            return jdbcTemplate.queryForObject(
                    queryFinder.findQuery(GET_COMMENTS_COUNT_FOR_POST_QUERY_NAME, JdbcNativePostRepository.class),
                    Integer.class,
                    postId);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("get comments count", exception).withPostId(postId).build();
        }
    }

    @Override
    public List<Post> getPostsByTag(String tag) {
        if (StringUtils.isBlank(tag)) {
            throw ExceptionBuilder.emptyInputParameters("get posts by tag").withTag(tag).build();
        }
        try {
            return jdbcTemplate.queryForStream(
                    queryFinder.findQuery(GET_POSTS_BY_TAG_QUERY_NAME, JdbcNativePostRepository.class),
                    postMapper,
                    tag).collect(Collectors.toList());
        } catch (Exception exception) {
            throw ExceptionBuilder.of("get posts by tag", exception).withTag(tag).build();
        }
    }

    @Override
    public List<Comment> getComments(UUID postId) {
        checkPostExistence(postId);
        try {
            return jdbcTemplate.queryForStream(
                    queryFinder.findQuery(GET_COMMENTS_BY_POST_ID_QUERY_NAME, JdbcNativePostRepository.class),
                    commentMapper,
                    postId).collect(Collectors.toList());
        } catch (Exception exception) {
            throw ExceptionBuilder.of("get comments", exception).withPostId(postId).build();
        }
    }

    @Override
    public void addLike(UUID id) {
        checkPostExistence(id);
        try {
            jdbcTemplate.update(
                    queryFinder.findQuery(INCREMENT_LIKES_QUERY_NAME, JdbcNativePostRepository.class),
                    id);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("add like", exception).withPostId(id).build();
        }
    }

    @Override
    public void removeLike(UUID id) {
        checkPostExistence(id);
        try {
            jdbcTemplate.update(
                    queryFinder.findQuery(DECREMENT_LIKES_QUERY_NAME, JdbcNativePostRepository.class),
                    id);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("remove like", exception).withPostId(id).build();
        }
    }

    @Override
    public void deletePost(UUID id) {
        int deletedRowsCount;
        try {
            deletedRowsCount = jdbcTemplate.update(
                    queryFinder.findQuery(DELETE_POST_QUERY_NAME, JdbcNativePostRepository.class),
                    id);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("delete post", exception).withPostId(id).build();
        }
        if (deletedRowsCount == 0) {
            throw ExceptionBuilder.incorrectInputParameters("delete post").withPostId(id).build();
        }
    }

    @Override
    public void addComment(UUID postId, String text) {
        checkPostExistence(postId);
        try {
            jdbcTemplate.update(
                    queryFinder.findQuery(ADD_COMMENT_QUERY_NAME, JdbcNativePostRepository.class),
                    postId, text);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("add comment", exception).withPostId(postId).withComment(text).build();
        }
    }

    @Override
    public void deleteComment(UUID commentId) {
        if (commentId == null) {
            throw ExceptionBuilder.emptyInputParameters("delete comment").withCommentId(commentId).build();
        }
        int deletedRowCount;
        try {
            deletedRowCount = jdbcTemplate.update(
                    queryFinder.findQuery(DELETE_COMMENT_QUERY_NAME, JdbcNativePostRepository.class),
                    commentId);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("delete comment", exception).withCommentId(commentId).build();
        }
        if (deletedRowCount == 0) {
            throw ExceptionBuilder.incorrectInputParameters("delete comment").withCommentId(commentId).build();
        }
    }

    @Override
    public void updateComment(UUID commentId, String text) {
        if (commentId == null || StringUtils.isEmpty(text)) {
            throw ExceptionBuilder.emptyInputParameters("update comment")
                    .withCommentId(commentId).withComment(text).build();
        }
        int updatedRowsCount;
        try {
            updatedRowsCount = jdbcTemplate.update(
                    queryFinder.findQuery(UPDATE_COMMENT_QUERY_NAME, JdbcNativePostRepository.class),
                    text, commentId);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("update comment", exception)
                    .withCommentId(commentId).withComment(text).build();
        }
        if (updatedRowsCount == 0) {
            throw ExceptionBuilder.incorrectInputParameters("update comment").withCommentId(commentId).build();
        }
    }

    @Override
    public UUID createPost(String name, String fullText, String shortText, UUID imageKey) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(fullText) || StringUtils.isBlank(shortText)) {
            throw ExceptionBuilder.emptyInputParameters("create post")
                    .withPostName(name).withFullText(fullText).withShortText(shortText).withImageId(imageKey).build();
        }
        try {
            return jdbcTemplate.queryForObject(
                    queryFinder.findQuery(ADD_POST_QUERY_NAME, JdbcNativePostRepository.class),
                    idMapper,
                    name, fullText, shortText, imageKey);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("create post", exception)
                    .withPostName(name).withFullText(fullText).withShortText(shortText).withImageId(imageKey).build();
        }
    }

    @Override
    public void updatePost(UUID id, String name, String fullText, String shortText, UUID imageKey) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(fullText) || StringUtils.isBlank(shortText)) {
            throw ExceptionBuilder.emptyInputParameters("create post")
                    .withPostName(name).withFullText(fullText).withShortText(shortText).withImageId(imageKey).build();
        }
        int updatedRowsCount;
        try {
            updatedRowsCount = jdbcTemplate.update(
                    queryFinder.findQuery(UPDATE_POST_QUERY_NAME, JdbcNativePostRepository.class),
                    name, fullText, shortText, imageKey, id);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("update post", exception).withPostId(id)
                    .withPostName(name).withFullText(fullText).withShortText(shortText).withImageId(imageKey).build();
        }
        if (updatedRowsCount == 0) {
            throw ExceptionBuilder.incorrectInputParameters("update post").withPostId(id)
                    .withPostName(name).withFullText(fullText).withShortText(shortText).withImageId(imageKey).build();
        }
    }

    @Override
    public void createTag(UUID postId, String tag) {
        checkPostExistence(postId);
        try {
            jdbcTemplate.update(
                    queryFinder.findQuery(ADD_TAG_QUERY_NAME, JdbcNativePostRepository.class),
                    postId, tag);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("create tag", exception).withPostId(postId).withTag(tag).build();
        }
    }

    @Override
    public void deleteTags(UUID postId, Collection<String> removedTags) {
        checkPostExistence(postId);
        if (CollectionUtils.isEmpty(removedTags)) {
            return;
        }
        try {
            jdbcTemplate.update(
                    queryFinder.findQuery(DELETE_TAG_QUERY_NAME, JdbcNativePostRepository.class),
                    ps -> {
                        ps.setObject(1, postId);
                        ps.setArray(2, ps.getConnection().createArrayOf(VARCHAR_TYPE,
                                removedTags.toArray()));
                    });
        } catch (Exception exception) {
            throw ExceptionBuilder.of("delete tags", exception)
                    .withPostId(postId).withTags(removedTags).build();
        }
    }

    private void checkPostExistence(UUID postId) {
        if (postId == null) {
            throw ExceptionBuilder.postIsNotFound(postId).build();
        }
        int count;
        try {
            count = jdbcTemplate.queryForObject(
                    queryFinder.findQuery(CHECK_POST_EXISTS_QUERY_NAME, JdbcNativePostRepository.class),
                    Integer.class,
                    postId);
        } catch (Exception exception) {
            throw ExceptionBuilder.of("check post existence", exception).withPostId(postId).build();
        }
        if (count == 0) {
            throw ExceptionBuilder.postIsNotFound(postId).build();
        }
    }
}
