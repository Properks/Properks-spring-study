package com.example.stomp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateAccountNickname {
    private Long id;
    private String nickname;
    private String code;

    public String getFullNickname() {
        return nickname + "#" + code;
    }
}
