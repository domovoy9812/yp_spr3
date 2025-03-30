package ru.yandex.practicum.bliushtein.spr3;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ru.yandex.practicum.bliushtein.spr3")
@PropertySource("classpath:application.properties")
public class WebConfiguration {}