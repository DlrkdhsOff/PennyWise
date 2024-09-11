package com.zero.pennywise.repository;

import com.zero.pennywise.entity.SavingsEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsRepository extends JpaRepository<SavingsEntity, Long> {

  Optional<SavingsEntity> findByUserIdAndName(Long userId, String name);

  Page<SavingsEntity> findByStartDate(LocalDate startDate, Pageable pageable);

  Page<SavingsEntity> findByEndDate(LocalDate endDate, Pageable pageable);

  boolean existsByUserIdAndName(Long userId, String name);
}
