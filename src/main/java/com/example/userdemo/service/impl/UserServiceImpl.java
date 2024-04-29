package com.example.userdemo.service.impl;

import com.example.userdemo.entity.User;
import com.example.userdemo.exception.IllegalUserFieldException;
import com.example.userdemo.external.UserDTO;
import com.example.userdemo.repository.UserRepository;
import com.example.userdemo.service.UserService;
import com.example.userdemo.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    @Value("${minAge}")
    private long minAge;
    
    @Override
    public UserDTO createUser(final UserDTO userDTO) {
        User savedUser = userRepository.save(UserMapper.toUser(userDTO));
        
        return UserMapper.toUserDTO(savedUser);

    }

    @Override
    public UserDTO patchUser(Integer id, Map<String, Object> updates) {
        return userRepository.findById(id).map(user -> {
            UserDTO userDTO = UserMapper.toUserDTO(user);
            updates.entrySet().forEach(entry-> setUserDTOfields(userDTO, entry));
            User toSave =  UserMapper.toUser(userDTO);
            toSave.setId(id);
            toSave.setCreatedAt(user.getCreatedAt());
            User savedUser = userRepository.save(toSave);
            return UserMapper.toUserDTO(savedUser);
        }).orElse(new UserDTO());
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDto) {
        return userRepository.findById(id).map(user -> {
            User toSave = UserMapper.toUser(userDto);
            toSave.setId(user.getId());
            toSave.setCreatedAt(user.getCreatedAt());
            User saved = userRepository.save(toSave);
            return UserMapper.toUserDTO(saved);
        }).orElse(new UserDTO());
    }

    @Override
    public String deleteUser(Integer id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return String.format("user with id %s deleted", id);
        }).orElse(String.format("user with id %s does not exist", id));
    }

    @Override
    public List<UserDTO> getAllUsers(LocalDate from, LocalDate to) {
        if (from.isAfter(to)){
            return Collections.emptyList();
        }
        return userRepository.findAllByBirthdayBetween(from, to).stream()
                .map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    private UserDTO setUserDTOfields(UserDTO userDTO, Map.Entry<String, Object> entry){
        switch (entry.getKey()){
            case "firstname":
                userDTO.setFirstname((String) entry.getValue());
                break;
            case "lastname":
                userDTO.setLastname((String) entry.getValue());
                break;
            case "email":
                userDTO.setEmail((String) entry.getValue());
                break;
            case "birthday":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse((String) entry.getValue(), formatter);
                LocalDate now = LocalDate.now();
                if (now.isBefore(date.plusYears(minAge))){
                throw new IllegalUserFieldException("Age less than 18");
            }
                userDTO.setBirthday(date);
                break;
            case "address":
                userDTO.setAddress((String) entry.getValue());
                break;
            case "phoneNumber":
                userDTO.setPhoneNumber((String) entry.getValue());
                break;
            default:
                throw new IllegalUserFieldException("unexpected field to update");
                
        }
        return userDTO;
    }
}
