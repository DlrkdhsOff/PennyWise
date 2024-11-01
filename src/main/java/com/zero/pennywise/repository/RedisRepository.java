package com.zero.pennywise.repository;

import com.zero.pennywise.entity.redis.BalanceEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<BalanceEntity, Long> {

  Optional<BalanceEntity> findByUserId(Long userId);

}
