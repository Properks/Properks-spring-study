package com.properk.blog.controller;

import com.properk.blog.dto.CreateAccessTokenRequest;
import com.properk.blog.dto.CreateAccessTokenResponse;
import com.properk.blog.service.RefreshTokenService;
import com.properk.blog.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createAccessToken(
            @RequestBody CreateAccessTokenRequest request) {
        CreateAccessTokenResponse response = new CreateAccessTokenResponse(
                tokenService.createNewAccessToken(request.getRefreshToken()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
