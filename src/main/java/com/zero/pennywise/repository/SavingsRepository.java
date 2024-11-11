package com.zero.pennywise.repository;

import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsRepository extends JpaRepository<SavingsEntity, Long> {

//  Optional<SavingsEntity> findByUserIdAndName(Long userId, String name);
//
//  Page<SavingsEntity> findByStartDate(LocalDate startDate, Pageable pageable);
//
//  Page<SavingsEntity> findByEndDate(LocalDate endDate, Pageable pageable);
//
//  boolean existsByUserIdAndName(Long userId, String name);
//
//  List<SavingsEntity> findAllByUserId(Long userId);

  void deleteAllByUser(UserEntity user);
}
