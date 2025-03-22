package com.zero.pennywise.core.service;

import com.zero.pennywise.domain.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AnalyzeService {

  ResultResponse analyzeIncomeAndExpenses(HttpServletRequest request);
}

