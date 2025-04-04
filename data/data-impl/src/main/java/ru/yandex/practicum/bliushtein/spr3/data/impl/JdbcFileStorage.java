package ru.yandex.practicum.bliushtein.spr3.data.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.bliushtein.spr3.data.repository.FileStorage;

import java.io.InputStream;
import java.util.UUID;

@Repository
public class JdbcFileStorage implements FileStorage {

    private final JdbcTemplate jdbcTemplate;

    public JdbcFileStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UUID saveFile(InputStream file) {
        return jdbcTemplate.query("""
                insert into binary_storage (data)
                values (?)
                returning key
                """,
                ps -> ps.setBinaryStream(1, file),
                (rs, rowNum) -> rs.getObject("key", UUID.class)
        ).getFirst();
    }

    @Override
    public void updateFile(UUID key, InputStream file) {
        jdbcTemplate.update("""
                update binary_storage
                set data = ?
                where key = ?
                """,ps -> {
            ps.setBinaryStream(1, file);
            ps.setObject(2, key);
        });
    }

    @Override
    public void deleteFile(UUID key) {
        jdbcTemplate.update("""
                delete from binary_storage
                where key = ?
                """, key);
    }

    @Override
    public InputStream getFileByKey(UUID key) {
        return jdbcTemplate.queryForObject("""
                select data from binary_storage
                where key = ?
                """,
                (rs, rowNum) -> rs.getBinaryStream("data"),
                key);
    }
}
