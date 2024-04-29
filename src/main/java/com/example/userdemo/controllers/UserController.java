package com.example.userdemo.controllers;

import com.example.userdemo.external.UserDTO;
import com.example.userdemo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class UserController {
  
  private final UserService userService;
  
  @PutMapping(value = "/create")
  public ResponseEntity<UserDTO> createUser(@Valid @RequestBody final UserDTO body){
    UserDTO response = userService.createUser(body);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PatchMapping(value = "/patch/{id}")
  public ResponseEntity<UserDTO> patchUser(@PathVariable final int id, 
  @RequestBody (required = false) final Map<String, Object> updates){
    UserDTO response = userService.patchUser(id, updates);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/update/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @Valid @RequestBody final UserDTO body){
    UserDTO response = userService.updateUser(id, body);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(value = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value = "to") LocalDate to){
    List<UserDTO> users = userService.getAllUsers(from, to);
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @DeleteMapping (value = "/delete/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable int id){
    String response = userService.deleteUser(id);
    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
  }
}
