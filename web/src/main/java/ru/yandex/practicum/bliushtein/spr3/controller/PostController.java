package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.bliushtein.spr3.service.PostService;
import ru.yandex.practicum.bliushtein.spr3.service.dto.ImageInfo;
import ru.yandex.practicum.bliushtein.spr3.service.dto.PostDetails;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService service;

    PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public String showPost(Model model, @PathVariable("id") UUID id) {
        PostDetails post = service.getPostDetails(id);
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") UUID id,
                             @RequestParam(value = "image_key", required = false) UUID imageKey) {
        service.deletePost(id, SpringImageInfo.forDelete(imageKey));
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
        model.addAttribute("post", new PostDetails());
        return "add-post";
    }

    @PostMapping("/{id}/edit")
    public String updatePost(Model model,
                             @PathVariable(name = "id") UUID id,
                             @RequestParam("tag") List<String> tags,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam(value = "image_key", required = false) UUID imageKey,
                             @RequestParam("text") String fullText,
                             @RequestParam("name") String name) {
        ImageInfo imageInfo = SpringImageInfo.forUpdate(image, imageKey);
        service.updatePost(id, name, fullText, tags, imageInfo);
        PostDetails post = service.getPostDetails(id);
        model.addAttribute("post", post);
        return "redirect:/post/" + id;
    }

    @PostMapping("/create")
    public String createPost(Model model,
                             @RequestParam(value = "tag", required = false) List<String> tags,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam("text") String fullText,
                             @RequestParam("name") String name) throws IOException {
        ImageInfo imageInfo = SpringImageInfo.forCreate(image);
        UUID id = service.createPost(name, fullText, tags, imageInfo);
        PostDetails post = service.getPostDetails(id);
        model.addAttribute("post", post);
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