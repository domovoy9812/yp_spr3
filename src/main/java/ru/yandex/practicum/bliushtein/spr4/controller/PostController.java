package ru.yandex.practicum.bliushtein.spr4.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.bliushtein.spr4.service.PostService;
import ru.yandex.practicum.bliushtein.spr4.service.dto.ImageOperation;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostDetails;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/post")
public class PostController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public String showPost(Model model, @PathVariable("id") UUID id) {
        LOGGER.info("get post by id={}", id);
        PostDetails post = service.getPostDetails(id);
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") UUID id,
                             @RequestParam(value = "image_key", required = false) UUID imageKey) {
        service.deletePost(id, SpringImageOperation.forDelete(imageKey));
        return "redirect:/feed";
    }

    @GetMapping("/{id}/edit")
    public String editPost(Model model, @PathVariable("id") UUID id) {
        PostDetails post = service.getPostDetails(id);
        model.addAttribute("post", post);
        return "add-post";
    }

    @GetMapping("/new")
    public String newPost(Model model) {
        model.addAttribute("post", PostDetails.EMPTY);
        return "add-post";
    }

    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable(name = "id") UUID id,
                             @RequestParam(value = "tag", required = false) List<String> tags,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam(value = "image_key", required = false) UUID imageKey,
                             @RequestParam("text") String fullText,
                             @RequestParam("name") String name) {
        ImageOperation imageOperation = SpringImageOperation.forUpdate(image, imageKey);
        service.updatePost(id, name, fullText, tags == null ? Collections.emptyList() : tags, imageOperation);
        return "redirect:/post/" + id;
    }

    @PostMapping("/create")
    public String createPost(@RequestParam(value = "tag", required = false) List<String> tags,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam("text") String fullText,
                             @RequestParam("name") String name) {
        ImageOperation imageOperation = SpringImageOperation.forCreate(image);
        UUID id = service.createPost(name, fullText, tags == null ? Collections.emptyList() : tags, imageOperation);
        return "redirect:/post/" + id;
    }

    @PostMapping("/{id}/like")
    public String addLike(Model model, @PathVariable("id") UUID id,
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