package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  boolean existsByEmail(String userId);

  UserEntity findByEmail(String userId);

  void deleteByEmail(String email);
}
