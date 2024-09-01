package com.assesment.web.converter;

import com.assesment.persistent.model.PersistentUserEntity;
import com.assesment.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoConverter {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public PersistentUserEntity dtoToEntity(UserDto userDto) {
        PersistentUserEntity persistentUserEntity = new PersistentUserEntity();
        persistentUserEntity.setName(userDto.getFirstName() + " " + userDto.getLastName());
        persistentUserEntity.setEmail(userDto.getEmail());
        persistentUserEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return persistentUserEntity;
    }

    public UserDto entityToDto(PersistentUserEntity entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getName().split(" ")[0]);
        dto.setLastName(entity.getName().split(" ")[1]);
        dto.setEmail(entity.getEmail());
        return dto;
    }

    public List<UserDto> entityToDtoList(List<PersistentUserEntity> entityList) {
        List<UserDto> dto = new ArrayList<>();
        dto = entityList.stream()
                .map((user) -> entityToDto(user))
                .collect(Collectors.toList());
        return dto;
    }

}
