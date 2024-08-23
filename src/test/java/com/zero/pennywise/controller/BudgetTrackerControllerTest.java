package com.zero.pennywise.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.pennywise.PennyWiseApplication;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.service.BudgetTrackerService;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = PennyWiseApplication.class)
@AutoConfigureMockMvc
class BudgetTrackerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BudgetRepository budgetRepository;

  @Autowired
  private CategoriesRepository categoriesRepository;

  @Autowired
  private BudgetTrackerService budgetTrackerService;

  private Long userId;

  @BeforeEach
  void setup() {
    // 테스트 데이터 설정
    userId = 1L;

    // 카테고리 생성
    CategoriesEntity category1 = new CategoriesEntity(null, "Food", true);
    CategoriesEntity category2 = new CategoriesEntity(null, "Transport", true);
    categoriesRepository.saveAll(Arrays.asList(category1, category2));

    // 예산 항목 생성
    BudgetEntity budget1 = new BudgetEntity(null, userId, category1.getCategoryId(), 10000L);
    BudgetEntity budget2 = new BudgetEntity(null, userId, category2.getCategoryId(), 5000L);
    budgetRepository.saveAll(Arrays.asList(budget1, budget2));
  }

  // 카테고리 목록 출력 테스트
  @Test
  @Transactional
  void testGetCategoryList() throws Exception {
    List<CategoriesEntity> expectedCategories = budgetTrackerService.getCategoryList(userId);

    mockMvc.perform(get("/budgets/categories")
            .sessionAttr("userId", userId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(expectedCategories)));
  }

  // 로그인이 안된 상태에서 카테고리 목록 요청 시도
  @Test
  void testGetCategoryList_Unauthorized() throws Exception {
    mockMvc.perform(get("/budgets/categories"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("로그인을 해주세요"));
  }
}