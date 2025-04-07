package ru.yandex.practicum.bliushtein.spr3.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.bliushtein.spr3.service.GetImageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Value("classpath:image/default_image.png")
    Resource defaultImage;

    private final GetImageService service;

    public ImageController(GetImageService service) {
        this.service = service;
    }

    @GetMapping("/{key}")
    public void getImage(HttpServletResponse response,
                         @PathVariable("key") UUID key) throws IOException {
        response.setHeader("content-type", "image/jpg");
        if (key == null) {
            return;
        }
        try (var output = response.getOutputStream()) {
            InputStream input = service.getImageByKey(key);
            if (input != null) {
                input.transferTo(output);
            }
        }
    }

    @GetMapping("/")
    public void getImage(HttpServletResponse response) throws IOException {
        response.setHeader("content-type", "image/jpg");
        try (var output = response.getOutputStream()) {
            defaultImage.getInputStream().transferTo(output);
        }
    }
}
