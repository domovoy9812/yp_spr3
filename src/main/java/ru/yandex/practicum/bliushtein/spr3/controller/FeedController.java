package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bliushtein.spr3.model.Post;
import ru.yandex.practicum.bliushtein.spr3.service.PostService;

import java.util.List;


@Controller
@RequestMapping("/feed")
public class FeedController {

    private final PostService service;

    public FeedController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public String users(Model model) {
        List<Post> posts = service.findAll();
        model.addAttribute("posts", posts);

        return "feed";
    }

}