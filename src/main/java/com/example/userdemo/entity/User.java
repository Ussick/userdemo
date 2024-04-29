package com.example.userdemo.entity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

import com.example.userdemo.annotations.MinAge;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String firstname;

  @Column(nullable = false)
  private String lastname;
  
  @Column(nullable = false)
  private String email;
  
  @Column(nullable = false)
  private LocalDate birthday;

  private String address;
  
  private String phoneNumber;

  @Column(nullable = false)
  private Instant createdAt;
  
  private Instant updateAt;

  @PrePersist
  public void prePersist() {
    if (this.createdAt != null) {
      return;
    }
    this.createdAt = Instant.now();
  }

  @PreUpdate
  public void preUpdate() {
    if (this.updateAt != null) {
      return;
    }
    this.updateAt = Instant.now();
  }
}
