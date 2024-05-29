package com.jeongmo.review_blog.dto.user;

import com.jeongmo.review_blog.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserResponse {
    private Long id;
    private String email;
    private String nickname;
    private String nicknameWithoutCode;
    private String nicknameCode;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.nicknameWithoutCode = user.getNicknameWithoutCode();
        this.nicknameCode = user.getNicknameCode();
    }
}
