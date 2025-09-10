package org.jeongmo.springabstraction.auth.token;

import org.jeongmo.springabstraction.domain.member.entity.Member;


public interface TokenUtil {
    String createAccessToken(Member member);
    String createRefreshToken(Member member);
    Long getUserId(String token);
}
