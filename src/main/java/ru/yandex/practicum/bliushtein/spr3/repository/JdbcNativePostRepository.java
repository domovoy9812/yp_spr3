package ru.yandex.practicum.bliushtein.spr3.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.bliushtein.spr3.model.Post;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

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
                        rs.getString("text"),
                        ZonedDateTime.from(rs.getDate("createdWhen").toInstant()),
                        rs.getObject("owner", UUID.class)
                ));
    }
}
