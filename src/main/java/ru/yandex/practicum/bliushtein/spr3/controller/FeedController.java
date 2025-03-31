package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bliushtein.spr3.core.service.PostService;
import ru.yandex.practicum.bliushtein.spr3.core.service.dto.PostSummary;

import java.util.List;


@Controller
@RequestMapping("/feed")
public class FeedController {

    private final PostService service;

    public FeedController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public String feed(Model model) {
        List<PostSummary> posts = service.findAll();
        model.addAttribute("posts", posts);
        return "feed";
    }

}