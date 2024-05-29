package com.properk.blog.repository;

import com.properk.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email); // find user info from user database by email.
    // Query is "FROM users WHERE email = #{email}"
}
