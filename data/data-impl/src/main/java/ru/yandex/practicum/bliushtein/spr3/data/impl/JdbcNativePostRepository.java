package ru.yandex.practicum.bliushtein.spr3.data.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.bliushtein.spr3.data.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.data.model.Post;
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
        return jdbcTemplate.query(
                queryFinder.findQuery(GET_ALL_POSTS_QUERY_NAME, JdbcNativePostRepository.class),
                postMapper);
    }

    @Override
    public Post getPost(UUID id) {
        return jdbcTemplate.queryForObject(
                queryFinder.findQuery(GET_POST_BY_ID_QUERY_NAME, JdbcNativePostRepository.class),
                postMapper, id);
    }

    @Override
    public List<String> getTags(UUID postId) {
        return jdbcTemplate.queryForStream(
                queryFinder.findQuery(GET_TAGS_BY_POST_ID_QUERY_NAME, JdbcNativePostRepository.class),
                tagMapper,
                postId).collect(Collectors.toList());
    }

    @Override
    public int getCommentsCount(UUID postId) {
        return jdbcTemplate.queryForObject(
                queryFinder.findQuery(GET_COMMENTS_COUNT_FOR_POST_QUERY_NAME, JdbcNativePostRepository.class),
                Integer.class,
                postId);
    }

    @Override
    public List<Post> getPostsByTag(String tag) {
        return jdbcTemplate.queryForStream(
                queryFinder.findQuery(GET_POSTS_BY_TAG_QUERY_NAME, JdbcNativePostRepository.class),
                postMapper,
                tag).collect(Collectors.toList());
    }

    @Override
    public List<Comment> getComments(UUID postId) {
        return jdbcTemplate.queryForStream(
                queryFinder.findQuery(GET_COMMENTS_BY_POST_ID_QUERY_NAME, JdbcNativePostRepository.class),
                commentMapper,
                postId).collect(Collectors.toList());
    }

    @Override
    public void addLike(UUID id) {
        jdbcTemplate.update(
                queryFinder.findQuery(INCREMENT_LIKES_QUERY_NAME, JdbcNativePostRepository.class),
                id);
    }

    @Override
    public void removeLike(UUID id) {
        jdbcTemplate.update(
                queryFinder.findQuery(DECREMENT_LIKES_QUERY_NAME, JdbcNativePostRepository.class),
                id);
    }

    @Override
    public void deletePost(UUID id) {
        jdbcTemplate.update(
                queryFinder.findQuery(DELETE_POST_QUERY_NAME, JdbcNativePostRepository.class),
                id);
    }

    @Override
    public void addComment(UUID postId, String text) {
        jdbcTemplate.update(
                queryFinder.findQuery(ADD_COMMENT_QUERY_NAME, JdbcNativePostRepository.class),
                postId, text);
    }

    @Override
    public void deleteComment(UUID commentId) {
        jdbcTemplate.update(
                queryFinder.findQuery(DELETE_COMMENT_QUERY_NAME, JdbcNativePostRepository.class),
                commentId);
    }

    @Override
    public void updateComment(UUID commentId, String text) {
        jdbcTemplate.update(
                queryFinder.findQuery(UPDATE_COMMENT_QUERY_NAME, JdbcNativePostRepository.class),
                text, commentId);
    }

    @Override
    public UUID createPost(String name, String fullText, String shortText, UUID imageKey) {
        return jdbcTemplate.queryForObject(
                queryFinder.findQuery(ADD_POST_QUERY_NAME, JdbcNativePostRepository.class),
                idMapper,
                name, fullText, shortText, imageKey);
    }

    @Override
    public void updatePost(UUID id, String name, String fullText, String shortText, UUID imageKey) {
        jdbcTemplate.update(
                queryFinder.findQuery(UPDATE_POST_QUERY_NAME, JdbcNativePostRepository.class),
                name, fullText, shortText, imageKey, id);
    }

    @Override
    public void createTag(UUID postId, String tag) {
        jdbcTemplate.update(
                queryFinder.findQuery(ADD_TAG_QUERY_NAME, JdbcNativePostRepository.class),
                postId, tag);
    }

    @Override
    public void deleteTags(UUID postId, Collection<String> removedTags) {
        jdbcTemplate.update(
                queryFinder.findQuery(DELETE_TAG_QUERY_NAME, JdbcNativePostRepository.class),
                ps -> {
                    ps.setObject(1, postId);
                    ps.setArray(2, ps.getConnection().createArrayOf(VARCHAR_TYPE,
                            removedTags.toArray()));
                });
    }
}
