package com.jeongmo.revise_blog.service;

import com.jeongmo.revise_blog.domain.User;
import com.jeongmo.revise_blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void signup(String email, String password, String nickname) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userRepository.save(User.builder()
                .email(email)
                        .password(encoder.encode(password))
                        .nickname(nickname)
                .build());
    }

}
