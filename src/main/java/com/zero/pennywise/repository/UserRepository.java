package com.zero.pennywise.repository;

import com.zero.pennywise.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByEmail(String email);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findById(long userId);

  boolean existsByNickname(String nickname);
}
