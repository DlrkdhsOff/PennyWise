package com.zero.pennywise.repository;

import com.zero.pennywise.entity.redis.BalanceEntity;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<BalanceEntity, Long> {

  BalanceEntity findByUserId(String userId);

}
