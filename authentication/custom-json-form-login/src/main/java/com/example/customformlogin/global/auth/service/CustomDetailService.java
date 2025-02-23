package com.example.customformlogin.global.auth.service;

import com.example.customformlogin.domain.member.entity.Member;
import com.example.customformlogin.domain.member.repository.MemberRepository;
import com.example.customformlogin.global.auth.principal.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾지 못했습니다."));
        return new CustomUserDetail(member);
    }
}
