package org.jeongmo.springabstraction.auth.filter;

import jakarta.persistence.EntityNotFoundException;
import org.jeongmo.springabstraction.auth.domain.CustomUserDetails;
import org.jeongmo.springabstraction.auth.token.JwtUtil;
import org.jeongmo.springabstraction.auth.token.TokenExtractor;
import org.jeongmo.springabstraction.domain.member.entity.Member;
import org.jeongmo.springabstraction.domain.member.repository.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtTokenAuthenticationFilter extends TokenAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public JwtTokenAuthenticationFilter(
            TokenExtractor tokenExtractor,
            JwtUtil jwtUtil,
            MemberRepository memberRepository
    ) {
        super(tokenExtractor);
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    protected Authentication attemptAuthentication(String token) throws Exception {
        Long id = jwtUtil.getUserId(token);
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("사용자를 찾지 못했습니다."));

        UserDetails userDetails = new CustomUserDetails(member);
        return UsernamePasswordAuthenticationToken.authenticated(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

}
