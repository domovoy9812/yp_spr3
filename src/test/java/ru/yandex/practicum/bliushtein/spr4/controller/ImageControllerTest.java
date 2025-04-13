package ru.yandex.practicum.bliushtein.spr4.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.bliushtein.spr4.service.GetImageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static ru.yandex.practicum.bliushtein.spr4.controller.configuration.TestData.*;

@WebMvcTest(ImageController.class)
public class ImageControllerTest {

    @MockitoBean
    private GetImageService imageServiceMock;

    @Value("classpath:image/test_image_1.png")
    private Resource inputImage;

    @Value("classpath:image/default_image.png")
    private Resource defaultImage;

    @Autowired
    private MockMvc mockMvc;
    @Test
    void test() throws Exception {
        when(imageServiceMock.getImageByKey(IMAGE_ID)).thenReturn(inputImage.getInputStream());
        mockMvc.perform(get("/image/{key}", IMAGE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpg"))
                .andDo(result -> assertArrayEquals(inputImage.getContentAsByteArray(),
                        result.getResponse().getContentAsByteArray()));
    }

    @Test
    void test_imageNotFound() throws Exception {
        when(imageServiceMock.getImageByKey(IMAGE_ID)).thenReturn(null);
        mockMvc.perform(get("/image/{key}", IMAGE_ID))
                .andExpect(status().isOk())
                .andDo(result -> assertEquals(0, result.getResponse().getContentAsByteArray().length));

    }

    @Test
    void test_imageNotFoundNoImageId() throws Exception {
        mockMvc.perform(get("/image/"))
                .andExpect(status().isOk())
                .andDo(result -> assertArrayEquals(defaultImage.getContentAsByteArray(),
                        result.getResponse().getContentAsByteArray()));
        verify(imageServiceMock, never()).getImageByKey(any());
    }
}
