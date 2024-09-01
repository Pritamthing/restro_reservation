package com.assesment.persistent.repository;

import com.assesment.persistent.model.PersistentTableEntity;
import com.assesment.web.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<PersistentTableEntity, Long> {
    PersistentTableEntity findTableByName(String name);

    List<PersistentTableEntity> findAllTableByStatus(TableStatus status);
}
