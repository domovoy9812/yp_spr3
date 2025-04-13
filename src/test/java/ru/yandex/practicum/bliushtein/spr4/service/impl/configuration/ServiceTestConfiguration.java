package ru.yandex.practicum.bliushtein.spr4.service.impl.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.bliushtein.spr4.data.repository.FileStorage;
import ru.yandex.practicum.bliushtein.spr4.data.repository.PostRepository;
import ru.yandex.practicum.bliushtein.spr4.service.ImageService;
import ru.yandex.practicum.bliushtein.spr4.service.PostService;
import ru.yandex.practicum.bliushtein.spr4.service.UpdateImageService;
import ru.yandex.practicum.bliushtein.spr4.service.impl.ImageServiceImpl;
import ru.yandex.practicum.bliushtein.spr4.service.impl.PostServiceImpl;

import static org.mockito.Mockito.mock;

@Configuration
public class ServiceTestConfiguration {

    @Bean
    public FileStorage fileStorage() {
        return mock(FileStorage.class);
    }

    @Bean
    @Primary
    public ImageService imageService(FileStorage fileStorage) {
        return new ImageServiceImpl(fileStorage);
    }

    @Bean("updateImageService")
    public UpdateImageService updateImageService() {
        return mock(UpdateImageService.class);
    }

    @Bean
    public PostRepository postRepository() {
        return mock(PostRepository.class);
    }

    @Bean
    public PostService postService(PostRepository postRepository, @Qualifier("updateImageService") UpdateImageService imageService) {
        return new PostServiceImpl(postRepository, imageService);
    }
}
