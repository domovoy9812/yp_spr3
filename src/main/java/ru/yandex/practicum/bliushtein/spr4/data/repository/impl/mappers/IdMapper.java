package ru.yandex.practicum.bliushtein.spr4.data.repository.impl.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class IdMapper implements RowMapper<UUID> {
    @Override
    public UUID mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getObject("id", UUID.class);
    }
}
