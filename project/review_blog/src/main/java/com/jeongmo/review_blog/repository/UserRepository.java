package com.jeongmo.review_blog.repository;

import com.jeongmo.review_blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by email
     *
     * @param email The email of user who you want to find
     * @return Return Optional User
     */
    Optional<User> findByEmail(String email);

    /**
     * Check whether user already exists
     *
     * @param email The email of user who you want to find
     * @return boolean. true: The email already exists
     */
    boolean existsByEmail(String email);

    /**
     * Check whether user who has same nickname already exists
     *
     * @param nickname The nickname of user who you want to find
     * @return boolean. true: The user already exists
     */
    boolean existsByNickname(String nickname);
}
