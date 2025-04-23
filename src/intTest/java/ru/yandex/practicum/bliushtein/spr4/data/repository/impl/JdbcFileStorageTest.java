package ru.yandex.practicum.bliushtein.spr4.data.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.sql.init.AbstractScriptDatabaseInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.bliushtein.spr4.data.repository.DataAccessException;
import ru.yandex.practicum.bliushtein.spr4.data.repository.FileStorage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class JdbcFileStorageTest {

    private final static UUID NOT_EXISTING_FILE_ID = new UUID(0, 0);

    @Autowired
    private FileStorage fileStorage;

    @Value("classpath:image/test_image_1.png")
    private Resource inputImage1;

    @Value("classpath:image/test_image_2.png")
    private Resource inputImage2;

    @Autowired
    private AbstractScriptDatabaseInitializer dbInitializer;

    @BeforeEach
    void setUp() {
        dbInitializer.initializeDatabase();
    }

    @Test
    void test(@TempDir Path tempDir) throws IOException {
        InputStream inputStream = spy(inputImage1.getInputStream());
        UUID id = fileStorage.saveFile(inputStream);
        verify(inputStream).close();
        Path resultImage1 = tempDir.resolve("result1.png");
        byte[] inputImage1ByteArray = inputImage1.getContentAsByteArray();
        Files.copy(fileStorage.getFile(id), resultImage1);
        assertArrayEquals(inputImage1ByteArray, Files.readAllBytes(resultImage1));

        fileStorage.updateFile(id, inputImage2.getInputStream());
        Path resultImage2 = tempDir.resolve("result2.png");
        Files.copy(fileStorage.getFile(id), resultImage2);
        byte[] resultImage2ByteArray = Files.readAllBytes(resultImage2);
        assertArrayEquals(inputImage2.getContentAsByteArray(), resultImage2ByteArray);
        assertFalse(Arrays.equals(inputImage1ByteArray, resultImage2ByteArray));

        fileStorage.deleteFile(id);
        assertNull(fileStorage.getFile(id));
    }

    @Test
    void testGetFile_fileNotFoundCases() {
        assertNull(fileStorage.getFile(null));
        assertNull(fileStorage.getFile(NOT_EXISTING_FILE_ID));
    }

    @Test
    void test_negative() throws IOException {
        assertDoesNotThrow(() -> fileStorage.deleteFile(NOT_EXISTING_FILE_ID));
        InputStream inputStream = spy(inputImage1.getInputStream());
        assertDoesNotThrow(() -> fileStorage.updateFile(NOT_EXISTING_FILE_ID, inputStream));
        verify(inputStream).close();

        assertThrows(DataAccessException.class, () -> fileStorage.saveFile(null));

        UUID id = fileStorage.saveFile(inputImage1.getInputStream());
        assertThrows(DataAccessException.class, () -> fileStorage.updateFile(id, null));
    }
}
