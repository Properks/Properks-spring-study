package com.example.customformlogin.domain.member.converter;

import com.example.customformlogin.domain.member.dto.response.MemberResponseDTO;
import com.example.customformlogin.domain.member.entity.Member;

public class MemberConverter {

    public static Member toMember(String username, String password) {
        return Member.builder()
                .username(username)
                .password(password)
                .build();
    }

    public static MemberResponseDTO.SignupResponseDTO toSignupResponseDTO(Member member) {
        return MemberResponseDTO.SignupResponseDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .build();
    }
}
