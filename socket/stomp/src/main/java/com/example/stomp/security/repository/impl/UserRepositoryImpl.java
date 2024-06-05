package com.example.stomp.security.repository.impl;

import com.example.stomp.security.domain.User;
import com.example.stomp.security.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {


    private final EntityManager entityManager;

    @Override
    @Transactional
    public User save(User user) {
        if (user.getId() != null && entityManager.find(User.class, user.getId()) != null) {
            entityManager.merge(user);
        }
        else {
            entityManager.persist(user);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return entityManager.createQuery("From User", User.class).getResultList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User foundUser = entityManager.find(User.class, id);
        if (foundUser != null) {
            entityManager.remove(foundUser);
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        List<User> users = entityManager.createQuery("FROM User", User.class).getResultList();
        for (User user : users) {
            deleteById(user.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        User foundUser = getUserByEmail(email);
        return Optional.ofNullable(foundUser);
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        return getUserByEmail(email) != null;
    }

    @Override
    @Transactional
    public boolean existsByNickname(String nickname) {
        return getUserByNickname(nickname) != null;
    }

    private User getUserByEmail(String email) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    private User getUserByNickname(String nickname) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname", User.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
    }
}
