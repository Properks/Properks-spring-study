package com.example.springrestdocs.controller;

import com.example.springrestdocs.apiPayload.ApiResponse;
import com.example.springrestdocs.service.HealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @GetMapping("/healthCheck")
    public ApiResponse<String> healthCheck() {
        String response = healthCheckService.check();
        return ApiResponse.onSuccess(response);
    }
}
