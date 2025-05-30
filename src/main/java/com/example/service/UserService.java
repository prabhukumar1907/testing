package com.example.service;

import com.example.dto.UserRequest;
import com.example.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User updateUser(Long id, UserRequest user);
    void deleteUser(Long id);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
}
