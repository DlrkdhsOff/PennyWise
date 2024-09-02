package com.zero.pennywise.repository;

import com.zero.pennywise.entity.WaringMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaringMessageRepository extends JpaRepository<WaringMessageEntity, Long> {

}
