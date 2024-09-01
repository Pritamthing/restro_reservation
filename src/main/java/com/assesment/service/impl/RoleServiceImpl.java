package com.assesment.service.impl;

import com.assesment.persistent.model.PersistentRoleEntity;
import com.assesment.persistent.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class RoleServiceImpl {

    @Autowired
    private RoleRepository roleRepository;

    public PersistentRoleEntity createRole(String name) {
        PersistentRoleEntity role = new PersistentRoleEntity();
        role.setName(name);
        return roleRepository.save(role);
    }
}
