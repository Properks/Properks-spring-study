package com.jeongmo.revise_blog.service;

import com.jeongmo.revise_blog.domain.User;
import com.jeongmo.revise_blog.dto.user.AddUserRequest;
import com.jeongmo.revise_blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(setNicknameWithCode(dto.getNickname()))
                .build());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Not Found " + email));
    }

    public boolean isDuplicatedEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private String setNicknameWithCode(String nickname) {
        int code = nickname.hashCode() % 10000;
        String newNickname = nickname + "#" + String.format("%04d", code);
        while(isDuplicatedNickname(newNickname)) {
            newNickname = nickname + "#" + String.format("%04d", ++code % 10000);
        }
        // FIXME: When 10000 same nickname. doesn't have code
        return newNickname;
    }

    private boolean isDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
