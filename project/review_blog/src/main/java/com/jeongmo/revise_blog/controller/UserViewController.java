package com.jeongmo.revise_blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class UserViewController {

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("login", "true");
        model.addAttribute("signup", "false");
        return "login";
    }

    @GetMapping("/signup")
    public String signUpPage(Model model) {
        model.addAttribute("signup", "true");
        model.addAttribute("login", "false");
        return "login";
    }
}
