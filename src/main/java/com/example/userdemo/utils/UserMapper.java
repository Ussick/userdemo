package com.example.userdemo.utils;

import com.example.userdemo.entity.User;
import com.example.userdemo.external.UserDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

  public static User toUser(final UserDTO userDto){
    User user = new User();
    user.setFirstname(userDto.getFirstname());
    user.setLastname(userDto.getLastname());
    user.setEmail(userDto.getEmail());
    user.setBirthday(userDto.getBirthday());
    user.setAddress(userDto.getAddress());
    user.setPhoneNumber(userDto.getPhoneNumber());
    return user;
  }

  public static UserDTO toUserDTO(final User user) {
    UserDTO userDTO = new UserDTO();
    userDTO.setFirstname(user.getFirstname());
    userDTO.setLastname(user.getLastname());
    userDTO.setEmail(user.getEmail());
    userDTO.setBirthday(user.getBirthday());
    userDTO.setAddress(user.getAddress());
    userDTO.setPhoneNumber(user.getPhoneNumber());
    return userDTO;
  }
}
