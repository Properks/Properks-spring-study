package org.jeongmo.springabstraction.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.jeongmo.springabstraction.domain.member.dto.MemberRequestDTO;
import org.jeongmo.springabstraction.domain.member.dto.MemberResponseDTO;
import org.jeongmo.springabstraction.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody MemberRequestDTO.SignUp request) {
        memberService.signUp(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDTO.Login> login(@RequestBody MemberRequestDTO.Login request) {
        MemberResponseDTO.Login response = memberService.login(request);
        return ResponseEntity.ok(response);
    }
}
