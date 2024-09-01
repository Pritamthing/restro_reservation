package com.assesment.persistent.repository;

import com.assesment.persistent.model.PersistentReservationEntity;
import com.assesment.persistent.model.PersistentTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<PersistentReservationEntity, Long> {
    PersistentReservationEntity findByTable(PersistentTableEntity table);

    List<PersistentReservationEntity> findAllByEndTimeIsNull();
    @Query("SELECT r FROM PersistentReservationEntity r WHERE r.table.id = :tableId AND r.endTime IS NULL")
    PersistentReservationEntity findActiveReservationsByTableId(@Param("tableId") Long tableId);
}
