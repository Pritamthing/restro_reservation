package com.assesment.persistent.repository;


import com.assesment.persistent.model.PersistentRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<PersistentRoleEntity, Long> {
    PersistentRoleEntity findByName(String name);
}