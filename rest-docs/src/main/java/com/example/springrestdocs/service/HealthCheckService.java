package com.example.springrestdocs.service;

import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    public String check() {
        return "성공";
    }
}
