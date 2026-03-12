package com.boot.bootSecurity.service;

import com.boot.bootSecurity.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    void deleteRole(Long id);

    void createRole(Role role);

    Role getRole(Long id);

    void updateRole(Long id, Role role);

}
