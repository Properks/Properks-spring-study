package com.properk.blog.controller;

import com.properk.blog.dto.AddUserRequest;
import com.properk.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signUp(@RequestBody AddUserRequest request) {
        userService.save(request);
        return "redirect:/login"; // redirect:/URL : Go to URL after completing request.
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }
}
