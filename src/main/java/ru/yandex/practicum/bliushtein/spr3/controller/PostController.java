package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bliushtein.spr3.core.service.PostService;
import ru.yandex.practicum.bliushtein.spr3.core.service.dto.PostDetails;

import java.util.UUID;


@Controller
@RequestMapping("/post")
public class PostController {
    private final PostService service;

    PostController(PostService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public String showPost(Model model, @PathVariable(name = "id") UUID id) {
        PostDetails post = service.getPostDetails(id);
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping(value = "/{id}/delete")
    public String deletePost(Model model, @PathVariable(name = "id") UUID id) {
        service.deletePost(id);
        return "redirect:/feed";
    }

    @PostMapping(value = "/{id}/edit")
    public String editPost(Model model, @PathVariable(name = "id") UUID id) {
        PostDetails post = service.getPostDetails(id);
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping(value = "/{id}/like")
    public String addLike(Model model, @PathVariable(name = "id") UUID id,
                          @RequestParam(name = "like") boolean addLike) {
        if (addLike) {
            service.addLike(id);
        } else {
            service.removeLike(id);
        }
        PostDetails post = service.getPostDetails(id);
        model.addAttribute("post", post);
        return "post";
    }
}