package ru.yandex.practicum.bliushtein.spr4.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.bliushtein.spr4.controller.configuration.ControllerTestConfiguration;
import ru.yandex.practicum.bliushtein.spr4.service.GetImageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static ru.yandex.practicum.bliushtein.spr4.controller.configuration.TestData.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = ControllerTestConfiguration.class)
public class ImageControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GetImageService imageServiceMock;

    @Value("classpath:image/test_image_1.png")
    private Resource inputImage;

    @Value("classpath:image/default_image.png")
    private Resource defaultImage;

    @Autowired
    private ImageController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        reset(imageServiceMock);
    }

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
