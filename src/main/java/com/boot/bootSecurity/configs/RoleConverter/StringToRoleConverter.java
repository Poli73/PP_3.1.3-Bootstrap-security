package com.boot.bootSecurity.configs.RoleConverter;

import com.boot.bootSecurity.model.Role;
import com.boot.bootSecurity.repositories.RoleRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    private final RoleRepository roleRepository;

    public StringToRoleConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role convert(String id) {
        return roleRepository.findById(Long.valueOf(id)).orElse(null);
    }
}