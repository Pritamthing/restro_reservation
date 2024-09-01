package com.assesment.utils;

import com.assesment.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTask {

    @Autowired
    private ReservationService reservationService;

    // Checks reservation status every 2 minute
    //@Scheduled(cron = "0 0/2 * * * ?")
    @Scheduled(fixedRate = 120000)
    public void executeTask() {
        reservationService.updateReservations();
    }
}