package com.zero.pennywise.repository.budget;

import java.util.List;

public interface BudgetQueryRepository {

  List<String> getAllCategory(Long userId, String page);

}