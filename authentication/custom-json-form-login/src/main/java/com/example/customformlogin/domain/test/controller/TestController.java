package com.example.customformlogin.domain.test.controller;

import com.example.customformlogin.global.payload.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public CustomResponse<String> test() {
        log.info("test 실행");
        return CustomResponse.onSuccess("성공");
    }
}
