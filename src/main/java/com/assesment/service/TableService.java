package com.assesment.service;

import com.assesment.web.dto.TableDto;

import java.util.List;

public interface TableService {
    void saveTable(TableDto dto);

    TableDto findTableByName(String name);

    List<TableDto> findAllTables();

    List<TableDto> findAllAvailableTablesByStatus();

}
