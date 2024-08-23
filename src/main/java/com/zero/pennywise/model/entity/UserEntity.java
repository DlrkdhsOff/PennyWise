package com.zero.pennywise.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserEntity extends DateEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50)
  private String email;

  @Column(nullable = false, length = 50)
  private String password;

  @Column(nullable = false, length = 50)
  private String username;

  @Column(nullable = false)
  private String phone;

//  @Column(nullable = false)
//  private LocalDate createdAt;

}
