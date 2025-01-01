package com.zero.pennywise.service;

import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CategoryService {

  ResultResponse getCategoryList(int page, HttpServletRequest request);

  ResultResponse createCategory(String categoryName, HttpServletRequest request);

  ResultResponse deleteCategory(String categoryName, HttpServletRequest request);
}
