package com.jeongmo.revise_blog.controller;

import com.jeongmo.revise_blog.dto.user.AddUserRequest;
import com.jeongmo.revise_blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signup(@RequestParam("email") String email, @RequestParam("password") String password,
                         @RequestParam("nickname") String nickname) {
        userService.save(new AddUserRequest(email, password, nickname));
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/api/email/{email}")
    public ResponseEntity<Void> validEmail(@PathVariable String email) {
        if (userService.isDuplicatedEmail(email)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        return ResponseEntity.ok().build();
    }
}
