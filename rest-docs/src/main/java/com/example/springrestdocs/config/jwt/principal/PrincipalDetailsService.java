package com.example.springrestdocs.config.jwt.principal;


import com.example.springrestdocs.entity.Member;
import com.example.springrestdocs.exception.MemberErrorCode;
import com.example.springrestdocs.exception.MemberException;
import com.example.springrestdocs.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    //loginId로 UserDetail 가져오기
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND));
        return new PrincipalDetails(member);
    }
}
