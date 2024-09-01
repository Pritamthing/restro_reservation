package com.assesment.persistent.repository;


import com.assesment.persistent.model.PersistentUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<PersistentUserEntity, Long> {

    PersistentUserEntity findByEmail(String email);

}