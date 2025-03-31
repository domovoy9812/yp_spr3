package ru.yandex.practicum.bliushtein.spr3.core.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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
    public List<Post> findAll() {
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
    public List<Tag> getTags(Post post) {
        //TODO select w/o stream -> list convert
        return jdbcTemplate.queryForStream("select name from tags where post = ?",
                (rs, rowNum) -> new Tag(
                        post.getId(),
                        rs.getString("name")
        ), post.getId()).collect(Collectors.toList());
    }

    @Override
    public int getCommentsCount(Post post) {
        return jdbcTemplate.queryForObject("select count(*) from comments where post = ?", Integer.class,
                post.getId());
    }
}
