package org.jeongmo.springabstraction.domain.member.converter;

import org.jeongmo.springabstraction.domain.member.dto.MemberRequestDTO;
import org.jeongmo.springabstraction.domain.member.dto.MemberResponseDTO;
import org.jeongmo.springabstraction.domain.member.entity.Member;

public class MemberConverter {

    public static Member toMember(String username, String encodedPassword) {
        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .build();
    }

    public static MemberResponseDTO.Login toLogin(String accessToken, String refreshToken) {
       return MemberResponseDTO.Login.builder()
               .accessToken(accessToken)
               .refreshToken(refreshToken)
               .build();
    }
}
