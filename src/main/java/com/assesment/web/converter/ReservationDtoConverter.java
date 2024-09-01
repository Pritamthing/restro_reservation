package com.assesment.web.converter;

import com.assesment.persistent.model.PersistentReservationEntity;
import com.assesment.utils.DateUtils;
import com.assesment.web.dto.ReservationDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationDtoConverter {

    public PersistentReservationEntity dtoToEntity(ReservationDto dto) {
        PersistentReservationEntity persistentReservationEntity = new PersistentReservationEntity();
        persistentReservationEntity.setReservationTime(LocalDateTime.now());
        persistentReservationEntity.setNumberOfCustomers(dto.getNumberOfCustomers());
        return persistentReservationEntity;
    }

    public ReservationDto entityToDto(PersistentReservationEntity entity) {
        ReservationDto dto = new ReservationDto();
        dto.setId(entity.getId());
        dto.setTableCapacity(entity.getTable().getCapacity());
        dto.setTableName(entity.getTable().getName());
        dto.setNumberOfCustomers(entity.getNumberOfCustomers());
        dto.setReservationTime(DateUtils.localDateTimeToString(entity.getReservationTime()));
        dto.setEndTime(DateUtils.localDateTimeToString(entity.getEndTime()));
        dto.setReservationBy(entity.getReservationBy().getName());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    public List<ReservationDto> entityToDtoList(List<PersistentReservationEntity> entityList) {
        List<ReservationDto> dto = new ArrayList<>();
        dto = entityList.stream()
                .map((user) -> entityToDto(user))
                .collect(Collectors.toList());
        return dto;
    }

}
