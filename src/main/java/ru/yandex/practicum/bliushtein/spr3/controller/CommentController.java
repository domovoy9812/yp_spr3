package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.bliushtein.spr3.core.service.PostService;

import java.util.UUID;

@Controller
@RequestMapping("/comment")
public class CommentController {
    private final PostService service;

    CommentController(PostService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public String addComment(Model model) {
        //TODO implement
        return "post";
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(Model model, @PathVariable(name = "id") UUID commentId) {
        //TODO implement
        return "post";
    }

    @PostMapping("/{id}/update")
    public String updateComment(Model model, @PathVariable(name = "id") UUID commentId) {
        //TODO implement
        return "post";
    }
}
