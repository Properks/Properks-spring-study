package com.example.customformlogin.domain.member.controller;

import com.example.customformlogin.domain.member.dto.request.MemberRequestDTO;
import com.example.customformlogin.domain.member.dto.response.MemberResponseDTO;
import com.example.customformlogin.domain.member.service.MemberService;
import com.example.customformlogin.global.payload.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public CustomResponse<MemberResponseDTO.SignupResponseDTO> signup(@RequestBody MemberRequestDTO.SignupRequestDTO dto) {
        return CustomResponse.onSuccess(memberService.signup(dto));
    }

}
