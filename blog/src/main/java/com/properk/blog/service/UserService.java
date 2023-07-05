package com.properk.blog.service;

import com.properk.blog.domain.User;
import com.properk.blog.dto.AddUserRequest;
import com.properk.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest request) {
        return userRepository.save(User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build()).getId();
    }
}
