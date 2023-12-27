package com.jeongmo.review_blog.controller;

import com.jeongmo.review_blog.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * Controller that user can see and modify user information
     *
     * @param tab The tab which user selects (tab = "my_info"or "change_password")
     */
    @GetMapping("/user/info")
    public String userInformation(Model model, Authentication authentication,
                                  @RequestParam(required = false, defaultValue = "my_info") String tab) {
        securityUtils.checkAndAddLoginInfo(model, authentication);
        if (!tab.equals("my_info") && !tab.equals("change_password")) {
            throw new IllegalArgumentException("Invalid tab name");
        } else {
            model.addAttribute("tab", tab);
        }
        return "userInformation";
    }
}
