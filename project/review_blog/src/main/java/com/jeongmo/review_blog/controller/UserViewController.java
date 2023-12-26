package com.jeongmo.review_blog.controller;

import com.jeongmo.review_blog.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class UserViewController {

    private final SecurityUtils securityUtils;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUpPage() {
        return "signup";
    }

    @GetMapping("/user/info")
    public String userInformation(Model model, Authentication authentication) {
        securityUtils.checkAndAddLoginInfo(model, authentication);
        return "userInformation";
    }
}
