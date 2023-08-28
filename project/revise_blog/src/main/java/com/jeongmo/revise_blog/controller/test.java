package com.jeongmo.revise_blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
