package com.assesment.web.dto;

import com.assesment.web.enums.TableStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TableDto {
    private Long id;

    @NotEmpty(message = "Table name should not be empty")
    private String name;

    @Min(value = 1, message = "Capacity must be greater than 0")
    private int capacity;

    private TableStatus status;
}
