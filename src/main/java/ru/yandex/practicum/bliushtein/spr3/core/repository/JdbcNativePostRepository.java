package ru.yandex.practicum.bliushtein.spr3.core.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.bliushtein.spr3.core.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.core.model.Post;
import ru.yandex.practicum.bliushtein.spr3.core.model.Tag;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> getAllPosts() {
        return jdbcTemplate.query(
                "select * from posts",
                (rs, rowNum) -> new Post(
                        rs.getObject("id", UUID.class),
                        rs.getString("name"),
                        rs.getString("full_text"),
                        //TODO add support for short text
                        rs.getString("full_text"),
                        ZonedDateTime.ofInstant(rs.getTimestamp("created_when").toInstant(),
                                ZoneId.systemDefault()),
                        rs.getInt("likes")
                ));
    }

    @Override
    public Post getPost(UUID id) {
        return jdbcTemplate.queryForObject("select * from posts where id = ?",
                (rs, rowNum) -> new Post(
                        rs.getObject("id", UUID.class),
                        rs.getString("name"),
                        rs.getString("full_text"),
                        //TODO add support for short text
                        rs.getString("full_text"),
                        ZonedDateTime.ofInstant(rs.getTimestamp("created_when").toInstant(),
                                ZoneId.systemDefault()),
                        rs.getInt("likes")
                ),
                id);
    }

    @Override
    public List<Tag> getTags(UUID postId) {
        //TODO select w/o stream -> list convert
        return jdbcTemplate.queryForStream("select name from tags where post = ?",
                (rs, rowNum) -> new Tag(
                        postId,
                        rs.getString("name")
        ), postId).collect(Collectors.toList());
    }

    @Override
    public int getCommentsCount(UUID postId) {
        return jdbcTemplate.queryForObject("select count(*) from comments where post = ?", Integer.class,
                postId);
    }

    @Override
    public List<Post> getPostByTag(String tag) {
        //TODO avoid code duplicate
        return jdbcTemplate.queryForStream("select posts.* from tags, posts where tags.name = ? and tags.post = posts.id",
                (rs, rowNum) -> new Post(
                        rs.getObject("id", UUID.class),
                        rs.getString("name"),
                        rs.getString("full_text"),
                        //TODO add support for short text
                        rs.getString("full_text"),
                        ZonedDateTime.ofInstant(rs.getTimestamp("created_when").toInstant(),
                                ZoneId.systemDefault()),
                        rs.getInt("likes")
                ), tag).collect(Collectors.toList());
    }

    @Override
    public List<Comment> getComments(UUID postId) {
        //TODO select w/o stream -> list convert
        return jdbcTemplate.queryForStream("select * from comments where post = ?",
                (rs, rowNum) -> new Comment(
                        rs.getObject("id", UUID.class),
                        rs.getString("text"),
                        ZonedDateTime.ofInstant(rs.getTimestamp("created_when").toInstant(),
                                ZoneId.systemDefault()),
                        postId
                ), postId).collect(Collectors.toList());
    }

    @Override
    public void addLike(UUID id) {
        jdbcTemplate.update("update posts set likes = likes + 1 where id = ?", id);
    }

    @Override
    public void removeLike(UUID id) {
        jdbcTemplate.update("update posts set likes = greatest(0, likes - 1) where id = ?", id);
    }

    @Override
    public void deletePost(UUID id) {
        jdbcTemplate.update("delete from posts where id = ?", id);
    }

    @Override
    public void addComment(UUID postId, String text) {
        jdbcTemplate.update("insert into comments (post, text) values (?, ?)", postId, text);
    }

    @Override
    public void deleteComment(UUID commentId) {
        jdbcTemplate.update("delete from comments where id = ?", commentId);
    }

    @Override
    public void updateComment(UUID commentId, String text) {
        jdbcTemplate.update("update comments set text = ? where id = ?", text, commentId);
    }
}
