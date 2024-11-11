package com.zero.pennywise.repository.querydsl.balance;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.balances.Balances;
import com.zero.pennywise.model.response.budget.Budgets;
import com.zero.pennywise.model.response.page.PageResponse;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceQueryRepository {

  List<Balances> getRecentAvg(UserEntity user);
}
