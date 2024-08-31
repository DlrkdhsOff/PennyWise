package com.zero.pennywise.repository.querydsl;

import com.zero.pennywise.model.dto.budget.BudgetDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetQueryRepository {

  Page<BudgetDTO> findAllBudgetByUserId(Long userId, Pageable pageable);
}