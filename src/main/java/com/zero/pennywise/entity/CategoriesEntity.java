package com.zero.pennywise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CategoriesEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long categoryId;

  @Column(nullable = false, unique = true)
  private String categoryName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

}
