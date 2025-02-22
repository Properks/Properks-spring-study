package com.example.customformlogin.domain.member.service;

import com.example.customformlogin.domain.member.converter.MemberConverter;
import com.example.customformlogin.domain.member.dto.request.MemberRequestDTO;
import com.example.customformlogin.domain.member.dto.response.MemberResponseDTO;
import com.example.customformlogin.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponseDTO.SignupResponseDTO signup(MemberRequestDTO.SignupRequestDTO dto) {
        return MemberConverter.toSignupResponseDTO(
                memberRepository.save(
                        MemberConverter.toMember(
                                dto.getUsername(),
                                passwordEncoder.encode(dto.getPassword())
                        )
                )
        );
    }
}
