package com.boot.bootSecurity.controllers;

import com.boot.bootSecurity.dto.UserResponseDto;
import com.boot.bootSecurity.model.User;

import com.boot.bootSecurity.model.UserDto;
import com.boot.bootSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/users")
public class UserRestController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponseDto> showAllUsers() {

        return userService.getAllUsersDto();

    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id
    ) {
        User user = userService.getUser(id);
        if (user == null) {
            throw new NoSuchElementException("THere is no user with id " + id);

        }
        return user;

    }

    @PostMapping
    public User createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);


    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
    }

}

