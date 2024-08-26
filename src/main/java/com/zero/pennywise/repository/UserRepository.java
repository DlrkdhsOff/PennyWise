package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByEmail(String userId);

  UserEntity findByEmail(String email);
}
