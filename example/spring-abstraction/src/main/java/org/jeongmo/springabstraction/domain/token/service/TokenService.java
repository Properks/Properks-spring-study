package org.jeongmo.springabstraction.domain.token.service;

import lombok.RequiredArgsConstructor;
import org.jeongmo.springabstraction.auth.token.JwtUtil;
import org.jeongmo.springabstraction.domain.member.converter.MemberConverter;
import org.jeongmo.springabstraction.domain.member.dto.MemberResponseDTO;
import org.jeongmo.springabstraction.domain.member.entity.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtUtil jwtUtil;

    public MemberResponseDTO.Login createLoginToken(Member member) {
        return MemberConverter.toLogin(
                jwtUtil.createAccessToken(member),
                jwtUtil.createRefreshToken(member)
        );
    }
}
