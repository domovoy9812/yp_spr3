package ru.yandex.practicum.bliushtein.spr4.data.repository.impl.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.bliushtein.spr4.data.model.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class CommentMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(
                rs.getObject("id", UUID.class),
                rs.getString("text"),
                ZonedDateTime.ofInstant(rs.getTimestamp("created_when").toInstant(),
                        ZoneId.systemDefault()),
                rs.getObject("post", UUID.class)
        );
    }
}
