package com.jeongmo.revise_blog.dto.user;

import lombok.Getter;

@Getter
public class AddUserRequest {
    private final String email;
    private final String password;
    private final String nickname;

    public AddUserRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
