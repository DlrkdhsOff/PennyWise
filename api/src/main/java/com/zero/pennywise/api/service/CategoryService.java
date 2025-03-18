package com.zero.pennywise.api.service;

import com.zero.pennywise.domain.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CategoryService {

  ResultResponse getCategoryList(HttpServletRequest request);

  ResultResponse createCategory(HttpServletRequest request, String categoryName);

  ResultResponse deleteCategory(HttpServletRequest request, String categoryName);
}
