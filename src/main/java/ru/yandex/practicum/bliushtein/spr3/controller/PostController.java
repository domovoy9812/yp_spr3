package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;


@Controller
@RequestMapping("/post")
public class PostController {

    @GetMapping(value = "/{id}")
    public String post(Model model, @PathVariable(name = "id") UUID id) {
        return "post";
    }

}