package org.jeongmo.springabstraction.domain.member.dto;

import lombok.Builder;

public record MemberResponseDTO() {

    @Builder
    public record Login(
            String accessToken,
            String refreshToken
    ) {

    }
}
