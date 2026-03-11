package com.boot.bootSecurity.init;

import com.boot.bootSecurity.model.Role;
import com.boot.bootSecurity.model.User;

import com.boot.bootSecurity.service.RoleService;
import com.boot.bootSecurity.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestLoader {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public TestLoader(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @PostConstruct
    @Transactional
    public void loadData() {
        if (!userService.getAllUsers().isEmpty()) {
            return;
        }

        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        roleService.createRole(adminRole);
        roleService.createRole(userRole);

        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setAge(35);
        admin.setEmail("admin@mail.ru");
        admin.setPassword("admin");
        admin.setRoles(Set.of(adminRole, userRole));

        userService.createUser(admin);

        User user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setAge(30);
        user.setEmail("user@mail.ru");
        user.setPassword("user");

        user.setRoles(Set.of(userRole));

        userService.createUser(user);
    }
}