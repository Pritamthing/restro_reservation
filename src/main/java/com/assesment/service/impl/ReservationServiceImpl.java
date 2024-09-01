package com.assesment.service.impl;

import com.assesment.persistent.model.PersistentReservationEntity;
import com.assesment.persistent.model.PersistentTableEntity;
import com.assesment.persistent.repository.ReservationRepository;
import com.assesment.persistent.repository.TableRepository;
import com.assesment.service.ReservationService;
import com.assesment.web.converter.ReservationDtoConverter;
import com.assesment.web.dto.ReservationDto;
import com.assesment.web.enums.ReservationStatus;
import com.assesment.web.enums.TableStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Transactional
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDtoConverter converter;

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public PersistentReservationEntity saveReservation(ReservationDto dto) {
        PersistentReservationEntity persistentReservationEntity = checkTheBestReservationBasedOnCustomerSize(dto);
        if (persistentReservationEntity != null) {
            return repository.save(persistentReservationEntity);
        }
        return null;
    }

    @Override
    public List<ReservationDto> findAllReservations() {
        List<PersistentReservationEntity> persistentReservationEntityList = repository.findAll();
        return converter.entityToDtoList(persistentReservationEntityList);
    }

    @Override
    public void updateReservations() {
        List<PersistentReservationEntity> reservationEntities = repository.findAllByEndTimeIsNull();
        if (reservationEntities.isEmpty()) {
            return;
        }
        for (PersistentReservationEntity entity : reservationEntities) {
            if (getTimeDifference(entity)) {
                entity.setEndTime(LocalDateTime.now());
                entity.setStatus(ReservationStatus.ENDED);
                PersistentTableEntity persistentTableEntity = entity.getTable();
                persistentTableEntity.setStatus(TableStatus.AVAILABLE);
                tableRepository.save(persistentTableEntity);
                repository.save(entity);
            }
        }
    }

    /**
     * Get the best fit table for customer reservation based on customer size
     * @param dto
     * @return
     */
    private PersistentReservationEntity checkTheBestReservationBasedOnCustomerSize(ReservationDto dto) {

        // 1. Find available tables:
        List<PersistentTableEntity> availableTables = tableRepository.findAllTableByStatus(TableStatus.AVAILABLE);

        // 2. at least one available table should exist
        if (availableTables.isEmpty()) {
            return null;
        }
        // 3. Find closest table by capacity:
        PersistentTableEntity closestTable = availableTables
                .stream().filter(table -> table.getCapacity() >= dto.getNumberOfCustomers())  // Filter by capacity
                .min(Comparator.comparingInt(PersistentTableEntity::getCapacity))  // Find minimum capacity
                .orElse(null);  // Return null if no suitable table found

        PersistentReservationEntity persistentReservationEntity = null;
        if (closestTable != null) {
            persistentReservationEntity = converter.dtoToEntity(dto);
            persistentReservationEntity.setTable(closestTable);
            persistentReservationEntity.setReservationBy(authenticationFacade.getCurrentUser());
            closestTable.setStatus(TableStatus.RESERVED);
            tableRepository.save(closestTable);
        } else {
            //4. get all the reserved tables and lookup for the best fit for re shuffle
            List<PersistentTableEntity> reservedTables = tableRepository.findAllTableByStatus(TableStatus.RESERVED);
            if (!reservedTables.isEmpty()) {
                // 5. Find closest table by capacity
                PersistentTableEntity closeTableByCapacity = reservedTables
                        .stream().filter(table -> table.getCapacity() >= dto.getNumberOfCustomers())  // Filter by capacity
                        .min(Comparator.comparingInt(PersistentTableEntity::getCapacity))  // Find minimum capacity
                        .orElse(null);
                // check reshuffle is possible if closeTableByCapacity exits
                if (closeTableByCapacity != null) {
                    persistentReservationEntity = checkReshuffle(closeTableByCapacity, availableTables, dto);

                    // no shuffle possible, reserve 2 or more table based on customer size and at least two tables should in available state
                    if (persistentReservationEntity == null && availableTables.size() >= 2) {
                        persistentReservationEntity = createTwoOrMoreReservation(availableTables, dto);
                    }
                } else {
                    // no closeTableByCapacity found, reserve two or more table
                    if (availableTables.size() >= 2) {
                        persistentReservationEntity = createTwoOrMoreReservation(availableTables, dto);
                    }
                }

            }

        }
        return persistentReservationEntity;
    }

    /**
     * To create more than one reservation, when customer requests for reservation maximum number
     * of customer than the maximum capacity of the table in a restaurant
     * @param availableTables, list of available tables for reservation
     * @param dto, request dto containing maximum customer
     * @return
     */
    private PersistentReservationEntity createTwoOrMoreReservation(List<PersistentTableEntity> availableTables, ReservationDto dto) {

        int totalCapacity = availableTables.stream()
                .mapToInt(PersistentTableEntity::getCapacity).sum();
        // reservation not possible, available tables capacity exceeds incoming customer size
        if (totalCapacity < dto.getNumberOfCustomers()) {
            return null;
        }
        PersistentReservationEntity persistentReservationEntity = null;

        int totalCustomer = dto.getNumberOfCustomers();
        while (totalCustomer > 0) {
            // 3. Find closest table by capacity:
            final int customer = totalCustomer;
            // find the maximum capacity table if the closet count table not found, else find the closest table by capacity, to maximize tht turnover
            PersistentTableEntity maxCapacityTable = availableTables
                    .stream()
                    .filter(table -> table.getCapacity() >= customer)
                    .min(Comparator.comparingInt(PersistentTableEntity::getCapacity))
                    .orElseGet(() -> availableTables.stream()
                            .max(Comparator.comparingInt(PersistentTableEntity::getCapacity))
                            .orElse(null));
            if (maxCapacityTable != null) {
                persistentReservationEntity = converter.dtoToEntity(dto);
                if (totalCustomer > maxCapacityTable.getCapacity()) {
                    persistentReservationEntity.setNumberOfCustomers(maxCapacityTable.getCapacity());
                } else {
                    persistentReservationEntity.setNumberOfCustomers(totalCustomer);
                }
                totalCustomer -= maxCapacityTable.getCapacity();
                persistentReservationEntity.setTable(maxCapacityTable);
                persistentReservationEntity.setReservationBy(authenticationFacade.getCurrentUser());
                repository.save(persistentReservationEntity);
                maxCapacityTable.setStatus(TableStatus.RESERVED);
                tableRepository.save(maxCapacityTable);
                availableTables.remove(maxCapacityTable);
            }
        }

        return persistentReservationEntity;

    }

    private PersistentReservationEntity checkReshuffle(PersistentTableEntity reservedEntity, List<PersistentTableEntity> availableTables, ReservationDto dto) {

        // 1. Find closest table by capacity for the already reserved table, to swap the table
        PersistentReservationEntity reservationEntity = repository.findActiveReservationsByTableId(reservedEntity.getId());
        if (reservationEntity == null) {
            return null;
        }
        PersistentTableEntity closestTable = availableTables
                .stream()
                .filter(table -> table.getCapacity() >= reservationEntity.getNumberOfCustomers())  // Filter by capacity
                .min(Comparator.comparingInt(PersistentTableEntity::getCapacity))  // Find minimum capacity
                .orElse(null);  // Return null if no suitable table found

        PersistentReservationEntity persistentReservationEntity = null;
        if (closestTable == null) {
            return null;
        } else {

            // 2. set closest table/ shaw table
            reservationEntity.setTable(closestTable);
            repository.save(reservationEntity);
            // 3. create new reservation
            persistentReservationEntity = converter.dtoToEntity(dto);
            persistentReservationEntity.setTable(reservedEntity);
            persistentReservationEntity.setReservationBy(authenticationFacade.getCurrentUser());
            closestTable.setStatus(TableStatus.RESERVED);
            persistentReservationEntity = repository.save(persistentReservationEntity);

        }
        return persistentReservationEntity;
    }


    private boolean getTimeDifference(PersistentReservationEntity reservationEntity) {
        LocalDateTime startDateTime = reservationEntity.getReservationTime();
        LocalDateTime endDateTime = LocalDateTime.now();

        Duration duration = Duration.between(startDateTime, endDateTime);
        long hours = duration.toHoursPart();
        System.out.println("Hours: " + hours);

        // reset the reservation every 2 hours
        if (hours >= 2) {
            return true;
        }
        return false;
    }
}
