package com.assesment.web.converter;

import com.assesment.persistent.model.PersistentTableEntity;
import com.assesment.web.dto.TableDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableDtoConverter {

    public PersistentTableEntity dtoToEntity(TableDto dto) {
        PersistentTableEntity persistentTableEntity = new PersistentTableEntity();
        persistentTableEntity.setName(dto.getName());
        persistentTableEntity.setCapacity(dto.getCapacity());
        if (dto.getStatus() != null) {
            persistentTableEntity.setStatus(dto.getStatus());
        }
        return persistentTableEntity;
    }

    public TableDto entityToDto(PersistentTableEntity entity) {
        TableDto dto = new TableDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCapacity(entity.getCapacity());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public List<TableDto> entityToDtoList(List<PersistentTableEntity> entityList) {
        List<TableDto> dto = new ArrayList<>();
        dto = entityList.stream()
                .map((user) -> entityToDto(user))
                .collect(Collectors.toList());
        return dto;
    }

}
