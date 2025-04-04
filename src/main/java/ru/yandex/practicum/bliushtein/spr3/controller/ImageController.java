package ru.yandex.practicum.bliushtein.spr3.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.bliushtein.spr3.core.service.PostService;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/image")
public class ImageController {

    private final PostService service;

    public ImageController(PostService service) {
        this.service = service;
    }

    @GetMapping("/{key}")
    public void getImage(HttpServletResponse response, @PathVariable(name="key") UUID key) throws IOException {
        response.setHeader("content-type", "image/jpg");
        try (var output = response.getOutputStream()) {
            service.getImageByKey(key).transferTo(output);
            response.getOutputStream().close();
        }

    }
}
