package com.example.customformlogin.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

public class MemberResponseDTO {
    @Builder
    @Getter
    public static class LoginResponseDTO {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    public static class SignupResponseDTO {
        private Long id;
        private String username;
    }
}
