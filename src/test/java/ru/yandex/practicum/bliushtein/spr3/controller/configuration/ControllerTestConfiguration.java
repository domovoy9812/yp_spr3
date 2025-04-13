package ru.yandex.practicum.bliushtein.spr3.controller.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.bliushtein.spr3.configuration.ThymeleafConfiguration;
import ru.yandex.practicum.bliushtein.spr3.controller.FeedController;
import ru.yandex.practicum.bliushtein.spr3.controller.ImageController;
import ru.yandex.practicum.bliushtein.spr3.controller.PostController;
import ru.yandex.practicum.bliushtein.spr3.service.GetImageService;
import ru.yandex.practicum.bliushtein.spr3.service.PostService;

@Configuration
@Import(ThymeleafConfiguration.class)
public class ControllerTestConfiguration {

    @Bean
    @Primary
    public FeedController feedController(PostService service) {
        return new FeedController(service);
    }

    @Bean
    @Primary
    public PostService postService() {
        return Mockito.mock(PostService.class);
    }

    @Bean
    @Primary
    public ImageController imageController(GetImageService service) {
        return new ImageController(service);
    }

    @Bean
    @Primary
    public GetImageService imageService() {
        return Mockito.mock(GetImageService.class);
    }

    @Bean
    @Primary
    public PostController postController(PostService service) {
        return new PostController(service);
    }
}
