package com.assesment.service.impl;

import com.assesment.persistent.model.PersistentRoleEntity;
import com.assesment.persistent.model.PersistentUserEntity;
import com.assesment.persistent.repository.RoleRepository;
import com.assesment.persistent.repository.UserRepository;
import com.assesment.service.UserService;
import com.assesment.web.converter.UserDtoConverter;
import com.assesment.web.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserDtoConverter userDtoConverter;
    @Autowired
    private RoleServiceImpl roleService;


    @Override
    public void saveUser(UserDto userDto) {
        PersistentUserEntity user = userDtoConverter.dtoToEntity(userDto);
        PersistentRoleEntity role = roleRepository.findByName(userDto.getRole().name());
        if (role == null) {
            role = roleService.createRole(userDto.getRole().name());
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }


    @Override
    public UserDto findUserByEmail(String email) {
        PersistentUserEntity persistentUserEntity = userRepository.findByEmail(email);
        if (persistentUserEntity != null) {
            return userDtoConverter.entityToDto(persistentUserEntity);
        } else {
            return null;
        }
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<PersistentUserEntity> users = userRepository.findAll();
        return userDtoConverter.entityToDtoList(users);
    }

}