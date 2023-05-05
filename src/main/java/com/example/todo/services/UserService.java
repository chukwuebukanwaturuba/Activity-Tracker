package com.example.todo.services;

import com.example.todo.models.User;
import dto.UserDTO;

public interface UserService {
    User registerUser(UserDTO userDTO);

    User authenticate(UserDTO userDTO);

    User findUserById(Long userId);
}
