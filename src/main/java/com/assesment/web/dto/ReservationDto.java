package com.assesment.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
public class ReservationDto {
    private Long id;

    private String tableName;

    private String reservationTime;

    private String endTime;

    @Min(value = 1, message = "Customers must be greater than 0")
    private int numberOfCustomers;

    private int tableCapacity;

    private String reservationBy;

    private String status;
}
