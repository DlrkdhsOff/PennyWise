package com.zero.pennywise.service;

import com.zero.pennywise.model.request.savings.SavingsDTO;
import com.zero.pennywise.model.response.ResultResponse;

public interface SavingsService {

  ResultResponse setSavings(SavingsDTO savingsDTO);
}
