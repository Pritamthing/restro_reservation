package com.assesment.service.impl;

import com.assesment.persistent.model.PersistentTableEntity;
import com.assesment.persistent.model.PersistentUserEntity;
import com.assesment.persistent.repository.TableRepository;
import com.assesment.service.TableService;
import com.assesment.web.converter.TableDtoConverter;
import com.assesment.web.dto.TableDto;
import com.assesment.web.enums.TableStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private TableDtoConverter converter;
    @Autowired
    private TableRepository repository;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public void saveTable(TableDto dto) {
        PersistentTableEntity persistentTableEntity = converter.dtoToEntity(dto);
        PersistentUserEntity persistentUserEntity = authenticationFacade.getCurrentUser();
        persistentTableEntity.setCreatedBy(persistentUserEntity);
        repository.save(persistentTableEntity);
    }

    @Override
    public TableDto findTableByName(String name) {
        PersistentTableEntity persistentTableEntity = repository.findTableByName(name);
        if (persistentTableEntity == null) {
            return null;
        }
        return converter.entityToDto(persistentTableEntity);
    }

    @Override
    public List<TableDto> findAllTables() {
        List<PersistentTableEntity> persistentTableEntityList = repository.findAll();
        return converter.entityToDtoList(persistentTableEntityList);
    }

    @Override
    public List<TableDto> findAllAvailableTablesByStatus() {
        List<PersistentTableEntity> persistentTableEntityList = repository.findAllTableByStatus(TableStatus.AVAILABLE);
        if (persistentTableEntityList == null) {
            return null;
        }
        return converter.entityToDtoList(persistentTableEntityList);
    }
}
