package com.example.customformlogin.domain.member.service;

import com.example.customformlogin.domain.member.dto.request.MemberRequestDTO;
import com.example.customformlogin.domain.member.dto.response.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO.SignupResponseDTO signup(MemberRequestDTO.SignupRequestDTO dto);
}
