package org.jeongmo.springabstraction.domain.member.dto;

public record MemberRequestDTO() {
    public record SignUp(
            String username,
            String password
    ) {

    }

    public record Login(
            String username,
            String password
    ) {

    }
}
