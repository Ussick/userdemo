package com.example.userdemo.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.example.userdemo.external.UserDTO;
import com.example.userdemo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(
    classes = {UserController.class})
@WebMvcTest(UserController.class)
@ExtendWith({MockitoExtension.class})
class UserControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private UserService userService;

  final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd");
  EasyRandom easyRandom = new EasyRandom();

  ObjectMapper objectMapper = new ObjectMapper();
  {
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  @SneakyThrows
  void getAllUsersTest(){
    UserDTO user1 = easyRandom.nextObject(UserDTO.class);
    List<UserDTO> users = List.of(user1);
    LocalDate toDate = LocalDate.now();
    LocalDate fromDate = toDate.minusDays(1L);
    String to = toDate.toString();
    String from = fromDate.toString();
        
    when(userService.getAllUsers(fromDate, toDate)).thenReturn(users);
    this.mockMvc.perform(get("/v1?from={from}&to={to}", from, to))
        .andExpect(status().isOk())

            .andExpect(jsonPath("$[0].firstname").value(users.get(0).getFirstname()))
            .andExpect(jsonPath("$.[0].lastname").value(users.get(0).getLastname()))
            .andExpect(jsonPath("$.[0].email").value(users.get(0).getEmail()))
            .andExpect(jsonPath("$.[0].birthday").value(users.get(0).getBirthday().format(dateTimeFormatter)))
            .andExpect(jsonPath("$.[0].address").value(users.get(0).getAddress()))
            .andExpect(jsonPath("$.[0].phoneNumber").value(users.get(0).getPhoneNumber()));

    verify(this.userService).getAllUsers(fromDate, toDate);
  }

  @Test
  @SneakyThrows
  void getDeleteUserTest(){
    when(userService.deleteUser(1)).thenReturn(any());
    this.mockMvc.perform(delete("/v1/delete/1"))
            .andExpect(status().isAccepted());

    verify(this.userService).deleteUser(1);
  }

  @Test
  @SneakyThrows
  void updateUserTest(){
    UserDTO body = easyRandom.nextObject(UserDTO.class);
    body.setBirthday(LocalDate.of(2000, 1,1));
    body.setEmail("test@test.com");
    when(userService.updateUser(any(), any())).thenReturn(body);

    this.mockMvc.perform(post("/v1/update/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstname").value(body.getFirstname()))
            .andExpect(jsonPath("$.lastname").value(body.getLastname()))
            .andExpect(jsonPath("$.email").value(body.getEmail()))
            .andExpect(jsonPath("$.birthday").value(body.getBirthday().format(dateTimeFormatter)))
            .andExpect(jsonPath("$.address").value(body.getAddress()))
            .andExpect(jsonPath("$.phoneNumber").value(body.getPhoneNumber()));

    verify(this.userService).updateUser(any(),any());
  }

  @Test
  @SneakyThrows
  void patchUserTest(){
    UserDTO body = easyRandom.nextObject(UserDTO.class);
    body.setBirthday(LocalDate.of(2000, 1,1));
    body.setEmail("test@test.com");
    when(userService.patchUser(any(), any())).thenReturn(body);

    this.mockMvc.perform(patch("/v1/patch/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(Map.of("firstname", "Ivan"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstname").value(body.getFirstname()))
            .andExpect(jsonPath("$.lastname").value(body.getLastname()))
            .andExpect(jsonPath("$.email").value(body.getEmail()))
            .andExpect(jsonPath("$.birthday").value(body.getBirthday().format(dateTimeFormatter)))
            .andExpect(jsonPath("$.address").value(body.getAddress()))
            .andExpect(jsonPath("$.phoneNumber").value(body.getPhoneNumber()));

    verify(this.userService).patchUser(any(),any());
  }

  @Test
  @SneakyThrows
  void createUserTest(){
    UserDTO body = easyRandom.nextObject(UserDTO.class);
    body.setBirthday(LocalDate.of(2000, 1,1));
    body.setEmail("test@test.com");
    when(userService.createUser(any())).thenReturn(body);

    this.mockMvc.perform(put("/v1/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstname").value(body.getFirstname()))
            .andExpect(jsonPath("$.lastname").value(body.getLastname()))
            .andExpect(jsonPath("$.email").value(body.getEmail()))
            .andExpect(jsonPath("$.birthday").value(body.getBirthday().format(dateTimeFormatter)))
            .andExpect(jsonPath("$.address").value(body.getAddress()))
            .andExpect(jsonPath("$.phoneNumber").value(body.getPhoneNumber()));

    verify(this.userService).createUser(any());
  }
}
