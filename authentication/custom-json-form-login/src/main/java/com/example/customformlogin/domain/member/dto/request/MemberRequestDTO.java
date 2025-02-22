package com.example.customformlogin.domain.member.dto.request;

import lombok.Getter;

public class MemberRequestDTO {

    @Getter
    public static class LoginRequestDTO {
        private String username;
        private String password;
    }

    @Getter
    public static class SignupRequestDTO {
        private String username;
        private String password;
    }
}
