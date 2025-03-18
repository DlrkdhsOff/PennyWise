package com.zero.pennywise.repository;

import com.zero.pennywise.entity.NotificationEntity;
import com.zero.pennywise.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

  Optional<List<NotificationEntity>> findAllByUser(UserEntity user);
}
