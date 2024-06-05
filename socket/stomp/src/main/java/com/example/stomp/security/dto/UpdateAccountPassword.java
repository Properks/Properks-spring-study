package com.example.stomp.security.dto;

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
