package ru.yandex.practicum.bliushtein.spr3.data.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.bliushtein.spr3.data.exceptions.ExceptionBuilder;
import ru.yandex.practicum.bliushtein.spr3.data.repository.FileStorage;

import java.io.InputStream;
import java.util.UUID;

@Repository
public class JdbcFileStorage implements FileStorage {

    private static final String SAVE_FILE_QUERY_NAME = "save_file";
    private static final String UPDATE_FILE_QUERY_NAME = "update_file";
    private static final String DELETE_FILE_QUERY_NAME = "delete_file";
    private static final String GET_FILE_QUERY_NAME = "get_file";

    private final JdbcTemplate jdbcTemplate;
    private final QueryFinder queryFinder;
    private final RowMapper<InputStream> binaryStreamMapper;
    private final RowMapper<UUID> idMapper;

    public JdbcFileStorage(JdbcTemplate jdbcTemplate, QueryFinder queryFinder,
                           @Qualifier("binaryStreamMapper") RowMapper<InputStream> binaryStreamMapper,
                           @Qualifier("idMapper") RowMapper<UUID> idMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryFinder = queryFinder;
        this.binaryStreamMapper = binaryStreamMapper;
        this.idMapper = idMapper;
    }

    @Override
    public UUID saveFile(InputStream file) {
        if (file == null) {
            throw ExceptionBuilder.emptyInputParameters("save file").withEmptyFile().build();
        }
        try (var localFile = file) {
            return jdbcTemplate.query(
                    queryFinder.findQuery(SAVE_FILE_QUERY_NAME, JdbcFileStorage.class),
                    ps -> ps.setBinaryStream(1, localFile),
                    idMapper
            ).getFirst();
        } catch (Exception exception) {
            throw ExceptionBuilder.of("save file", exception).build();
        }
    }

    @Override
    public void updateFile(UUID id, InputStream file) {
        if (file == null) {
            throw ExceptionBuilder.emptyInputParameters("update file").withEmptyFile().build();
        } else try (var localFile = file) {
            jdbcTemplate.update(
                    queryFinder.findQuery(UPDATE_FILE_QUERY_NAME, JdbcFileStorage.class),
                    ps -> {
                        ps.setBinaryStream(1, localFile);
                        ps.setObject(2, id);
                    });
        } catch (Exception exception) {
            throw ExceptionBuilder.of("update file", exception).build();
        }
    }

    @Override
    public void deleteFile(UUID id) {
        jdbcTemplate.update(
                queryFinder.findQuery(DELETE_FILE_QUERY_NAME, JdbcFileStorage.class),
                id);
    }

    @Override
    public InputStream getFile(UUID id) {
        if (id == null) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(
                    queryFinder.findQuery(GET_FILE_QUERY_NAME, JdbcFileStorage.class),
                    binaryStreamMapper,
                    id);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }
}
