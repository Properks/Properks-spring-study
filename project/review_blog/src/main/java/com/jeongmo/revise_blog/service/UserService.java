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

    /**
     * Save user with password encoding
     *
     * @param dto The information of user will add
     */
    public void save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(setNicknameWithCode(dto.getNickname()))
                .build());
    }

    /**
     * Find user with Email
     *
     * @param email The email to find user
     * @throws IllegalArgumentException If it can't find user by email
     * @return The found user
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Not Found " + email));
    }

    /**
     * Check email is already exist
     *
     * @param email The email to check redundancy
     * @return boolean, true: The email is already exist
     */
    public boolean isDuplicatedEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Add random code behind the nickname
     *
     * @param nickname nickname which doesn't have code (nickname)
     * @return nickname which have code (nickname#0000 form)
     */
    private String setNicknameWithCode(String nickname) {
        int code = nickname.hashCode() % 10000;
        String newNickname = nickname + "#" + String.format("%04d", code);
        while(isDuplicatedNickname(newNickname)) {
            newNickname = nickname + "#" + String.format("%04d", ++code % 10000);
        }
        // FIXME: When 10000 same nickname. doesn't have code
        return newNickname;
    }

    /**
     * Check whether nickname which have code is already exist
     *
     * @param nickname The nickname which have code to check
     * @return boolean, true: The nickname is already exist
     */
    private boolean isDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
