package com.example.springrestdocs.controller;

import com.example.springrestdocs.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
//@RequiredArgsConstructor
public class TestController {

//    private final TestService testService;

    @GetMapping
    public ResponseEntity<String> test() {
//        return ResponseEntity.ok(testService.test());
        return ResponseEntity.ok("성공");
    }
}
