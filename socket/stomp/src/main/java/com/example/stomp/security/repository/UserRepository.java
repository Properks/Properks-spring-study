package com.example.stomp.security.repository;

import com.example.stomp.security.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    void deleteAll();

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

//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//    /**
//     * Find user by email
//     *
//     * @param email The email of user who you want to find
//     * @return Return Optional User
//     */
//    Optional<User> findByEmail(String email);
//
//    /**
//     * Check whether user already exists
//     *
//     * @param email The email of user who you want to find
//     * @return boolean. true: The email already exists
//     */
//    boolean existsByEmail(String email);
//
//    /**
//     * Check whether user who has same nickname already exists
//     *
//     * @param nickname The nickname of user who you want to find
//     * @return boolean. true: The user already exists
//     */
//    boolean existsByNickname(String nickname);
//}