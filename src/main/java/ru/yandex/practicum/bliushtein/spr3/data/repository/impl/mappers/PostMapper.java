package ru.yandex.practicum.bliushtein.spr3.data.repository.impl.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.bliushtein.spr3.data.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class PostMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Post(
                rs.getObject("id", UUID.class),
                rs.getString("name"),
                rs.getString("full_text"),
                rs.getString("short_text"),
                ZonedDateTime.ofInstant(rs.getTimestamp("created_when").toInstant(),
                        ZoneId.systemDefault()),
                rs.getInt("likes"),
                rs.getObject("image_key", UUID.class)
        );
    }
}
