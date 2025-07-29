package org.jeongmo.springabstraction.domain.member.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jeongmo.springabstraction.domain.member.converter.MemberConverter;
import org.jeongmo.springabstraction.domain.member.dto.MemberRequestDTO;
import org.jeongmo.springabstraction.domain.member.dto.MemberResponseDTO;
import org.jeongmo.springabstraction.domain.member.entity.Member;
import org.jeongmo.springabstraction.domain.member.repository.MemberRepository;
import org.jeongmo.springabstraction.domain.token.service.TokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public void signUp(MemberRequestDTO.SignUp request) {
        memberRepository.save(MemberConverter.toMember(request.username(), passwordEncoder.encode(request.password())));
    }

    public MemberResponseDTO.Login login(MemberRequestDTO.Login request) {
        Member member = memberRepository.findByUsername(request.username()).orElseThrow(() ->
                new EntityNotFoundException("사용자가 없습니다."));
        if (passwordEncoder.matches(request.password(), member.getPassword())) {
            return tokenService.createLoginToken(member);
        }
        else {
            throw new BadCredentialsException("비밀번호가 다릅니다.");
        }
    }
}
