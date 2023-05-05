package com.example.todo.repository;

import com.example.todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmailAndPassword(String email,String password);
    Optional<User> findByEmail(String email);

    User findUserById(Long userId);
}
