package com.example.stomp.security.controller;

import com.example.stomp.security.domain.User;
import com.example.stomp.security.dto.AddUserRequest;
import com.example.stomp.security.dto.UserResponse;
import com.example.stomp.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

//    @PostMapping("/users")
//    public String signup(@RequestParam("email") String email, @RequestParam("password") String password,
//                         @RequestParam("nickname") String nickname) {
//        userService.save(new AddUserRequest(email, password, nickname));
//        return "redirect:/login";
//    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok().body(new UserResponse((User) authentication.getPrincipal()));
        }
        return ResponseEntity.badRequest().build();
    }
}
