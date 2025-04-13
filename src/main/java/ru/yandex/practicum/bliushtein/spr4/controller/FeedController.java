package ru.yandex.practicum.bliushtein.spr4.controller;

import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bliushtein.spr4.service.PostService;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostSummary;

import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/feed")
public class FeedController {

    private final PostService service;

    public FeedController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public String feed(HttpSession session,
                       @RequestParam(name = "tag", required = false) String tag,
                       @RequestParam(name = "pageSize", required = false, defaultValue = "50") int pageSize,
                       @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber) {
        String searchTag = (String) session.getAttribute("tag");
        PagedListHolder<PostSummary> pagedPosts = (PagedListHolder<PostSummary>) session.getAttribute("posts");
        if (needReloadPosts(tag, searchTag, pagedPosts)) {
            List<PostSummary> posts = StringUtils.isBlank(tag) ? service.findAll() : service.findByTag(tag);
            pagedPosts = new PagedListHolder<>(posts);
            session.setAttribute("posts", pagedPosts);
            session.setAttribute("tag", tag);
        }
        pagedPosts.setPageSize(pageSize);
        pagedPosts.setPage(pageNumber);
        return "feed";
    }

    private static boolean needReloadPosts(String tag, String searchTag, PagedListHolder<PostSummary> posts) {
        return !Objects.equals(searchTag, tag) || posts == null;
    }
}