package com.example.userdemo.service.impl;

import com.example.userdemo.entity.User;
import com.example.userdemo.exception.IllegalUserFieldException;
import com.example.userdemo.external.UserDTO;
import com.example.userdemo.repository.UserRepository;
import com.example.userdemo.utils.UserMapper;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest  {
    
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    EasyRandom easyRandom = new EasyRandom();
    
    @Test
    void createUserTest() {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        User user = easyRandom.nextObject(User.class);
        when(userRepository.save(any())).thenReturn(user);
        UserDTO actual = userService.createUser(userDTO);
        
        assertThat(actual.getAddress()).isEqualTo(user.getAddress());
        assertThat(actual.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(actual.getEmail()).isEqualTo(user.getEmail());
        assertThat(actual.getFirstname()).isEqualTo(user.getFirstname());
        assertThat(actual.getLastname()).isEqualTo(user.getLastname());
        assertThat(actual.getBirthday()).isEqualTo(user.getBirthday());

        verify(userRepository).save(any());
    }

    @Test
    void patchUserTest() {
        User user = easyRandom.nextObject(User.class);
        
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertThrows(IllegalUserFieldException.class, () -> userService.patchUser(1, Map.of("lastnam", "test")));
        
        verify(userRepository).findById(any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void patchUserExceptionTest() {
        User user = easyRandom.nextObject(User.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        UserDTO actual = userService.patchUser(1, Map.of("lastname", "test"));
        
        assertThat(actual.getAddress()).isEqualTo(user.getAddress());
        assertThat(actual.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(actual.getEmail()).isEqualTo(user.getEmail());
        assertThat(actual.getFirstname()).isEqualTo(user.getFirstname());
        assertThat(actual.getLastname()).isEqualTo(user.getLastname());
        assertThat(actual.getBirthday()).isEqualTo(user.getBirthday());

        verify(userRepository).findById(any());
        verify(userRepository).save(any());
    }

    @Test
    void updateUserTest() {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        User user = easyRandom.nextObject(User.class);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        UserDTO actual = userService.updateUser(1, userDTO);
        assertThat(actual.getAddress()).isEqualTo(user.getAddress());
        
        assertThat(actual.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(actual.getEmail()).isEqualTo(user.getEmail());
        assertThat(actual.getFirstname()).isEqualTo(user.getFirstname());
        assertThat(actual.getLastname()).isEqualTo(user.getLastname());
        assertThat(actual.getBirthday()).isEqualTo(user.getBirthday());

        verify(userRepository).findById(any());
        verify(userRepository).save(any());
    }

    @Test
    void updateUserEmptyUserTest() {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        User user = easyRandom.nextObject(User.class);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(null));
        UserDTO actual = userService.updateUser(1, userDTO);
        
        assertThat(actual.getAddress()).isNull();
        assertThat(actual.getPhoneNumber()).isNull();
        assertThat(actual.getEmail()).isNull();
        assertThat(actual.getFirstname()).isNull();
        assertThat(actual.getLastname()).isNull();
        assertThat(actual.getBirthday()).isNull();

        verify(userRepository).findById(any());
    }

    @Test
    void deleteUserTest() {
        User user = easyRandom.nextObject(User.class);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        String actual = userService.deleteUser(1);
        
        assertThat(actual).isEqualTo("user with id 1 deleted");
        verify(userRepository).findById(any());
        verify(userRepository).delete(any());
    }

    @Test
    void deleteUserNoUserTest() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(null));
        String actual = userService.deleteUser(1);
        
        assertThat(actual).isEqualTo("user with id 1 does not exist");
        verify(userRepository).findById(any());
    }

    @Test
    void getAllUsersTest() {
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(1);
        List<User> users = List.of(easyRandom.nextObject(User.class), easyRandom.nextObject(User.class));
        
        when(userRepository.findAllByBirthdayBetween(from, to)).thenReturn(users);

        List<UserDTO> actual = userService.getAllUsers(from, to);

        assertThat(actual).isNotEmpty().hasSize(2);
        assertThat(actual.stream().map(UserDTO::getAddress).collect(Collectors.toList())).
                containsExactlyInAnyOrder(users.get(0).getAddress(), users.get(1).getAddress());
        assertThat(actual.stream().map(UserDTO::getPhoneNumber).collect(Collectors.toList())).
                containsExactlyInAnyOrder(users.get(0).getPhoneNumber(), users.get(1).getPhoneNumber());
        assertThat(actual.stream().map(UserDTO::getEmail).collect(Collectors.toList())).
                containsExactlyInAnyOrder(users.get(0).getEmail(), users.get(1).getEmail());
        assertThat(actual.stream().map(UserDTO::getBirthday).collect(Collectors.toList())).
                containsExactlyInAnyOrder(users.get(0).getBirthday(), users.get(1).getBirthday());
        assertThat(actual.stream().map(UserDTO::getFirstname).collect(Collectors.toList())).
                containsExactlyInAnyOrder(users.get(0).getFirstname(), users.get(1).getFirstname());
        assertThat(actual.stream().map(UserDTO::getLastname).collect(Collectors.toList())).
                containsExactlyInAnyOrder(users.get(0).getLastname(), users.get(1).getLastname());
        
        verify(userRepository).findAllByBirthdayBetween(any(), any());

    }
}
