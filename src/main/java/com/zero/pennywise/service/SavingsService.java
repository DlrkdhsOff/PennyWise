package com.zero.pennywise.service;

import com.zero.pennywise.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface SavingsService {

  ResultResponse recommend(HttpServletRequest request);
}
