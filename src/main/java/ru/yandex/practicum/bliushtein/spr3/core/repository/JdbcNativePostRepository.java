package ru.yandex.practicum.bliushtein.spr3.core.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.bliushtein.spr3.core.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.core.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
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
                "select * from posts order by created_when desc",
                JdbcNativePostRepository::createPostByResultSet);
    }

    private static Post createPostByResultSet(ResultSet rs, int rownum) throws SQLException {
        return new Post(
                rs.getObject("id", UUID.class),
                rs.getString("name"),
                rs.getString("full_text"),
                rs.getString("short_text"),
                ZonedDateTime.ofInstant(rs.getTimestamp("created_when").toInstant(),
                        ZoneId.systemDefault()),
                rs.getInt("likes")
        );
    }

    @Override
    public Post getPost(UUID id) {
        return jdbcTemplate.queryForObject("select * from posts where id = ? order by created_when desc",
                JdbcNativePostRepository::createPostByResultSet, id);
    }

    @Override
    public List<String> getTags(UUID postId) {
        //TODO select w/o stream -> list convert
        return jdbcTemplate.queryForStream("select name from tags where post = ?",
                (rs, rowNum) -> rs.getString("name"), postId).collect(Collectors.toList());
    }

    @Override
    public int getCommentsCount(UUID postId) {
        return jdbcTemplate.queryForObject("""
                        select count(*)
                        from comments
                        where post = ?
                        """, Integer.class,
                postId);
    }

    @Override
    public List<Post> getPostByTag(String tag) {
        return jdbcTemplate.queryForStream("""
                        select posts.*
                        from tags, posts
                        where tags.name = ?
                            and tags.post = posts.id
                        order by posts.created_when desc
                        """,
                JdbcNativePostRepository::createPostByResultSet, tag).collect(Collectors.toList());
    }

    @Override
    public List<Comment> getComments(UUID postId) {
        //TODO select w/o stream -> list convert
        return jdbcTemplate.queryForStream("select * from comments where post = ? order by created_when desc",
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

    @Override
    public UUID createPost(String name, String fullText, String shortText) {
        return jdbcTemplate.queryForObject("""
                        insert into posts (name, full_text, short_text)
                        values (?, ?, ?)
                        returning id
                        """,
                (rs, rowNum) -> rs.getObject("id", UUID.class),
                name, fullText, shortText);
    }

    @Override
    public void updatePost(UUID id, String name, String fullText, String shortText) {
        jdbcTemplate.update("""
                update posts
                set
                    name = ?,
                    full_text = ?,
                    short_text = ?
                where id = ?""", name, fullText, shortText, id);
    }

    @Override
    public void createTag(UUID postId, String tag) {
        jdbcTemplate.update("insert into tags (post, name) values (?, ?)", postId, tag);
    }

    @Override
    public void deleteTags(UUID postId, Collection<String> removedTags) {
        jdbcTemplate.update("delete from tags where post = ? and name in (SELECT UNNEST(?))"
                , ps -> {
                    ps.setObject(1, postId);
                    ps.setArray(2, ps.getConnection().createArrayOf("varchar",
                            removedTags.toArray()));
                });
    }
}
