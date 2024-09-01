package com.assesment.service;


import com.assesment.web.dto.UserDto;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    UserDto findUserByEmail(String email);

    List<UserDto> findAllUsers();

}