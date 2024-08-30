//package com.zero.pennywise.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zero.pennywise.PennyWiseApplication;
//import com.zero.pennywise.model.dto.BudgetDTO;
//import com.zero.pennywise.model.dto.CategoryDTO;
//import com.zero.pennywise.model.dto.TransactionDTO;
//import com.zero.pennywise.service.BudgetTrackerService;
//import jakarta.transaction.Transactional;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest(classes = PennyWiseApplication.class)
//@AutoConfigureMockMvc
//class BudgetTrackerControllerTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @Autowired
//  private ObjectMapper objectMapper;
//
//  @Autowired
//  private BudgetTrackerService budgetTrackerService;
//
//  private Long userId;
//
//  @BeforeEach
//
//  void setup() {
//    userId = 1L;
//  }
//
//  // 카테고리 목록 출력 테스트
//  @Test
//  @Transactional
//  void testGetCategoryList() throws Exception {
//    List<String> expectedCategories = budgetTrackerService.getCategoryList(userId);
//
//    mockMvc.perform(get("/budgets/categories")
//            .sessionAttr("userId", userId))
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(content().json(objectMapper.writeValueAsString(expectedCategories)));
//  }
//
//  // 로그인이 안된 상태에서 카테고리 목록 요청 시도
//  @Test
//  void testGetCategoryList_Unauthorized() throws Exception {
//    mockMvc.perform(get("/budgets/categories"))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string("로그인을 해주세요"));
//  }
//
//  // 카테고리 추가 테스트
//  @Test
//  @Transactional
//  void testCreateCategory() throws Exception {
//    CategoryDTO newCategory = new CategoryDTO();
//    newCategory.setCategoryName("요리");
//
//    // 응답 상태 및 메시지 확인
//    mockMvc.perform(post("/budgets/create-category")
//            .sessionAttr("userId", userId)
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(newCategory)))
//        .andExpect(status().isCreated())
//        .andExpect(content().string("카테고리를 생성하였습니다."));
//  }
//
//  // 이미 존재하는 카테고리를 추가하려는 경우
//  @Test
//  @Transactional
//  void testCreateCategory_AlreadyExists() throws Exception {
//    CategoryDTO existingCategory = new CategoryDTO();
//    existingCategory.setCategoryName("통신비");
//
//    Response response = budgetTrackerService.createCategory(userId, existingCategory);
//
//    // 응답 상태 및 메시지 확인
//    mockMvc.perform(post("/budgets/create-category")
//            .sessionAttr("userId", userId)
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(existingCategory)))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(response.getMessage()));
//  }
//
//  // 카테고리별 예산 설정
//  @Test
//  @Transactional
//  void testSetBudget_Success() throws Exception {
//    BudgetDTO budgetDTO = new BudgetDTO();
//    budgetDTO.setCategoryName("월급");
//    budgetDTO.setAmount(100000L);
//
//    Response response = budgetTrackerService.setBudget(userId, budgetDTO);
//
//    mockMvc.perform(patch("/budgets/set-budget")
//        .sessionAttr("userId", userId)
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsString(budgetDTO)))
//        .andExpect(status().isOk())
//        .andExpect(content().string(response.getMessage()));
//
//  }
//
//  // 존재하지 않은 카테고리 일 경우
//  @Test
//  @Transactional
//  void testSetBudget_NotFoundCategory() throws Exception {
//    BudgetDTO budgetDTO = new BudgetDTO();
//    budgetDTO.setCategoryName("쇼핑");
//    budgetDTO.setAmount(100000L);
//
//    Response response = budgetTrackerService.setBudget(userId, budgetDTO);
//
//    mockMvc.perform(patch("/budgets/set-budget")
//            .sessionAttr("userId", userId)
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(budgetDTO)))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(response.getMessage()));
//
//  }
//
//  // 수입 / 지출 내역 등록
//  @Test
//  @Transactional
//  void testTransaction_Success() throws Exception {
//    TransactionDTO transactionDTO = new TransactionDTO();
//    transactionDTO.setType("수입");
//    transactionDTO.setCategoryName("월급");
//    transactionDTO.setAmount(300000L);
//    transactionDTO.setDescription("회사 월급");
//    transactionDTO.setIsFixed("Y");
//
//    Response response = budgetTrackerService.transaction(userId, transactionDTO);
//
//    mockMvc.perform(post("/budgets/transaction")
//            .sessionAttr("userId", userId)
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(transactionDTO)))
//        .andExpect(status().isOk())
//        .andExpect(content().string(response.getMessage()));
//
//  }
//
//  // 수입 / 지출 내역 등록 : 존재하지 않은 카테고리
//  @Test
//  @Transactional
//  void testTransaction_NotFoundCategory() throws Exception {
//    TransactionDTO transactionDTO = new TransactionDTO();
//    transactionDTO.setType("수입");
//    transactionDTO.setCategoryName("쇼핑");
//    transactionDTO.setAmount(300000L);
//    transactionDTO.setDescription("회사 월급");
//    transactionDTO.setIsFixed("Y");
//
//    Response response = budgetTrackerService.transaction(userId, transactionDTO);
//
//    mockMvc.perform(post("/budgets/transaction")
//            .sessionAttr("userId", userId)
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(transactionDTO)))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(response.getMessage()));
//
//  }
//}