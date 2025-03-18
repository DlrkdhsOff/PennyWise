package com.zero.pennywise.service;

import com.zero.pennywise.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CategoryService {

  ResultResponse getCategoryList(HttpServletRequest request);

  ResultResponse createCategory(HttpServletRequest request, String categoryName);

  ResultResponse deleteCategory(HttpServletRequest request, String categoryName);
}
