package ru.yandex.practicum.bliushtein.spr3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/home")
    @ResponseBody
    public String homePage() {
        return "<h1>Hello, world!</h1>"; // Ответ
    }

}