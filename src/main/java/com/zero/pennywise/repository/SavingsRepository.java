package com.zero.pennywise.repository;

import com.zero.pennywise.entity.SavingsEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsRepository extends JpaRepository<SavingsEntity, Long> {

  Optional<SavingsEntity> findByUserIdAndName(Long userId, String name);
}
