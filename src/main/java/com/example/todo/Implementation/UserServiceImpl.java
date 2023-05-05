package com.example.todo.Implementation;

import com.example.todo.services.UserService;
import com.example.todo.models.User;
import com.example.todo.repository.UserRepository;
import dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(UserDTO userDTO){
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()){
            throw new RuntimeException("Email Already exist " + userDTO.getEmail());
        }
        User user = new User(userDTO);
        return userRepository.save(user);
    }

    @Override
    public User authenticate(UserDTO userDTO){
        User user = userRepository.findUserByEmailAndPassword(userDTO.getEmail(),userDTO.getPassword());
        if (user!=null) {
            return user;
        }else{
            return null;
        }

    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }
}
