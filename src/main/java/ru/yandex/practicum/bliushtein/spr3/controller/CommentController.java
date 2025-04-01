package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.bliushtein.spr3.core.service.PostService;

import java.util.UUID;

@Controller
@RequestMapping("/post/{postId}/comment")
public class CommentController {
    private final PostService service;

    CommentController(PostService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public String addComment(Model model, @PathVariable(name = "postId") UUID postId,
                             @RequestParam(name="text") String text) {
        service.addComment(postId, text);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(Model model, @PathVariable(name = "id") UUID commentId,
                                @PathVariable(name = "postId") UUID postId) {
        service.deleteComment(commentId);
        return "redirect:/post/" + postId;

    }

    @PostMapping("/{id}/update")
    public String updateComment(Model model, @PathVariable(name = "id") UUID commentId,
                                @RequestParam(name="text") String text,
                                @PathVariable(name = "postId") UUID postId) {
        service.updateComment(commentId, text);
        return "redirect:/post/" + postId;
    }
}
