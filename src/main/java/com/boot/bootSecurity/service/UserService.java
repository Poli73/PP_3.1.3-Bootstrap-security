package com.boot.bootSecurity.service;


import com.boot.bootSecurity.model.User;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserService {

    List<User> getAllUsers();

    User getUser(Long id);

    void createUser(User user);

    void updateUser(Long id, User user);

    void deleteUser(Long id);

    User findByEmail(String email);
}
