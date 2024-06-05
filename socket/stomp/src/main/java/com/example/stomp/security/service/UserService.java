package com.example.stomp.security.service;

import com.example.stomp.security.domain.User;
import com.example.stomp.security.dto.AddUserRequest;
import com.example.stomp.security.dto.UpdateAccountNickname;
import com.example.stomp.security.dto.UpdateAccountPassword;
import com.example.stomp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * Save user with password encoding
     *
     * @param dto The information of user will add
     */
    public User save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(setNicknameWithCode(dto.getNickname()))
                .build());
    }


    /**
     * Delete user with id
     *
     * @param id The id of user who will be deleted.
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
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
     * Find user by id
     *
     * @param id The id of user which you find
     * @return The found user (Else -> null)
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Check whether email already exists or not
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
        int code = nickname.hashCode() % 100000;
        String newNickname = nickname + "#" + String.format("%05d", code);
        while(isDuplicatedNickname(newNickname)) {
            newNickname = nickname + "#" + String.format("%05d", ++code % 100000);
        }
        return newNickname;
    }

    /**
     * Update user nickname
     *
     * @param dto The password dto
     * @return The updated user
     */
    @Transactional
    public User updateNickname(UpdateAccountNickname dto) {
        User updatedUser = getUserById(dto.getId());
        if (updatedUser == null || (!updatedUser.getNickname().equals(dto.getFullNickname()) && isDuplicatedNickname(dto.getFullNickname()))) {
            return null;
        }
        updatedUser.setNickname(dto.getFullNickname());
        return updatedUser;
    }

    @Transactional
    public User updatePassword(UpdateAccountPassword dto) {
        User updatedUser = getUserById(dto.getId());
        if (updatedUser == null || !isValidPassword(updatedUser.getId(), dto.getOldPassword())) {
            return null;
        }
        updatedUser.setPassword(encoder.encode(dto.getNewPassword()));
        return updatedUser;
    }

    /**
     * Check whether nickname which have code already exist
     *
     * @param nickname The nickname which have code to check
     * @return boolean, true: The nickname is already exist
     */
    private boolean isDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean isValidPassword(Long id, String password) {
        User user = getUserById(id);
        return encoder.matches(password, user.getPassword());
    }
}
