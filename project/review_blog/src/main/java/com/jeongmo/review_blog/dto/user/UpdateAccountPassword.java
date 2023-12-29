package com.jeongmo.review_blog.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateAccountPassword {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
