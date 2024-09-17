package com.zero.pennywise.entity.redis;

import com.zero.pennywise.model.request.budget.BalancesDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "Balance", timeToLive = 86400)
public class BalanceEntity {

  @Id
  private String userId;

  private List<BalancesDTO> balances;

}
