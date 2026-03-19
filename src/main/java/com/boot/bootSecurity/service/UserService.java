package com.boot.bootSecurity.service;


import com.boot.bootSecurity.dto.UserResponseDto;
import com.boot.bootSecurity.model.Role;
import com.boot.bootSecurity.model.User;
import com.boot.bootSecurity.model.UserDto;
import com.boot.bootSecurity.repositories.RoleRepository;
import com.boot.bootSecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
    }


    public User createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setYearOfBirth(userDto.getYearOfBirth());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Ищем роли по имени
        Set<Role> roles = userDto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        return userRepository.save(user);
    }


    public User updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDto.getUsername());
        user.setYearOfBirth(userDto.getYearOfBirth());

        // Если есть пароль — хешируем
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        // Ищем роли по имени
        Set<Role> roles = userDto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }


    public List<UserResponseDto> getAllUsersDto() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserResponseDto dto = new UserResponseDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setYearOfBirth(user.getYearOfBirth());
                    dto.setRoles(user.getRoles().stream()
                            .map(Role::getName)
                            .toList());
                    return dto;
                })
                .toList();
    }

}
