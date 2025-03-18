package com.zero.pennywise.repository;

import com.zero.pennywise.entity.UserEntity;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByEmail(String email);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByUserId(long userId);

  boolean existsByNickname(String nickname);

  @Modifying
  @Transactional
  @Query("UPDATE users u SET u.nickname = :nickname WHERE u.userId = :userId")
  void updateUserNickname(Long userId, String nickname);

  @Modifying
  @Transactional
  @Query("UPDATE users u SET u.password = :password WHERE u.userId = :userId")
  void updateUserPassword(Long userId, String password);
}
