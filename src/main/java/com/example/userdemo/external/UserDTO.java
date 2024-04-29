package com.example.userdemo.external;

import java.time.LocalDate;

import com.example.userdemo.annotations.MinAge;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

  @NotNull
  private String firstname;

  @NotNull
  private String lastname;

  @NotNull
  @Email
  private String email;
  
  @NotNull
  @MinAge
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthday;

  private String address;

  private String phoneNumber;
}
