package com.assesment.service;

import com.assesment.persistent.model.PersistentReservationEntity;
import com.assesment.web.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    PersistentReservationEntity saveReservation(ReservationDto dto);

    List<ReservationDto> findAllReservations();

    void updateReservations();

}
