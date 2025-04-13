package ru.yandex.practicum.bliushtein.spr4.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.bliushtein.spr4.data.repository.FileStorage;
import ru.yandex.practicum.bliushtein.spr4.service.ImageService;
import ru.yandex.practicum.bliushtein.spr4.service.dto.ImageOperation;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {ImageServiceImpl.class})
public class ImageServiceImplTest {

    private final static UUID IMAGE_ID = new UUID(0, 0);

    @MockitoBean
    private FileStorage fileStorageMock;

    @Mock
    private ImageOperation operationMock;

    @Mock
    private InputStream streamMock;

    @Autowired
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        reset(operationMock);
    }

    @Test
    void testProcessImage_addImage() throws IOException {
        when(operationMock.getInputStream()).thenReturn(streamMock);
        when(operationMock.getAction()).thenReturn(ImageOperation.Action.ADD);
        when(fileStorageMock.saveFile(streamMock)).thenReturn(IMAGE_ID);
        Assertions.assertEquals(IMAGE_ID, imageService.processImage(operationMock));
    }

    @Test
    void testProcessImage_noChange() {
        when(operationMock.getAction()).thenReturn(ImageOperation.Action.SAME);
        when(operationMock.getKey()).thenReturn(IMAGE_ID);
        Assertions.assertEquals(IMAGE_ID, imageService.processImage(operationMock));
        verify(fileStorageMock, never()).saveFile(any());
        verify(fileStorageMock, never()).updateFile(any(), any());
        verify(fileStorageMock, never()).deleteFile(any());
    }

    @Test
    void testProcessImage_deleteImage() {
        when(operationMock.getAction()).thenReturn(ImageOperation.Action.DELETE);
        when(operationMock.getKey()).thenReturn(IMAGE_ID);
        Assertions.assertNull(imageService.processImage(operationMock));
        verify(fileStorageMock).deleteFile(IMAGE_ID);
    }

    @Test
    void testProcessImage_updateImage() throws IOException {
        when(operationMock.getInputStream()).thenReturn(streamMock);
        when(operationMock.getAction()).thenReturn(ImageOperation.Action.UPDATE);
        when(operationMock.getKey()).thenReturn(IMAGE_ID);
        Assertions.assertEquals(IMAGE_ID, imageService.processImage(operationMock));
        verify(fileStorageMock).updateFile(IMAGE_ID, streamMock);
    }
}
