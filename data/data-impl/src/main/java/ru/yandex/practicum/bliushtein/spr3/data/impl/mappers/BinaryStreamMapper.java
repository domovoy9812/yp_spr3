package ru.yandex.practicum.bliushtein.spr3.data.impl.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BinaryStreamMapper implements RowMapper<InputStream> {
    @Override
    public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getBinaryStream("data");
    }
}
