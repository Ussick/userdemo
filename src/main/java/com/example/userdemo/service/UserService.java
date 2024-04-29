package com.example.userdemo.service;

import com.example.userdemo.external.UserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {

    UserDTO createUser(UserDTO body);
    
    UserDTO patchUser(Integer id, Map<String, Object> updates);

    UserDTO updateUser(Integer id, UserDTO userDto);

    String deleteUser(Integer id);

    List<UserDTO> getAllUsers(LocalDate from, LocalDate to);
}
