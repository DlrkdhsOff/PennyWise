package com.zero.pennywise.component.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zero.pennywise.auth.jwt.JwtUtil;
import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.enums.TransactionType;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.request.budget.UpdateBudgetDTO;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.request.user.LoginDTO;
import com.zero.pennywise.model.request.user.SignUpDTO;
import com.zero.pennywise.model.request.user.UpdateDTO;
import com.zero.pennywise.model.response.budget.Budgets;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.response.transaction.Transactions;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.model.type.UserRole;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoryRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.querydsl.budget.BudgetQueryRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class FacadeManagerTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private BudgetRepository budgetRepository;

  @Mock
  private TransactionQueryRepository transactionQueryRepository;

  @Mock
  private BudgetQueryRepository budgetQueryRepository;

  @Mock
  private HttpServletRequest request;

  @Mock
  private JwtUtil jwtUtil;

  @Mock
  private BCryptPasswordEncoder encoder;

  @InjectMocks
  private FacadeManager facadeManager;


  private final Long USER_ID = 1L;
  private final Long CATEGORY_ID = 1L;
  private final Long TRANSACTION_ID = 1L;
  private final Long BUDGET_ID = 1L;

  private final String EMAIL = "user@example.com";
  private final String PASSWORD = "123456";
  private final String NEW_PASSWORD = "1098765";
  private final String ENCODE_PASSWORD = "encodePassword";
  private final String NICKNAME = "nickname";
  private final UserRole USER_ROLE = UserRole.USER;

  private final String CATEGORY_NAME = "categoryName";
  private final String NEW_CATEGORY_NAME = "newCategoryName";
  private final int PAGE_NUM = 1;


  private UserEntity user;
  private SignUpDTO signUpDTO;
  private LoginDTO loginDTO;
  private UpdateDTO updateDTO;

  private CategoryEntity category;
  private List<CategoryEntity> categoryEntities;
  private List<String> categories;
  private UpdateCategoryDTO updateCategoryDTO;


  private List<Transactions> transactions;
  private TransactionInfoDTO transactionInfoDTO;
  private TransactionDTO transactionDTO;
  private TransactionEntity transaction;

  private List<Budgets> budgetList;
  private BudgetDTO budgetDTO;
  private UpdateBudgetDTO updateBudgetDTO;
  private BudgetEntity budget;

  @BeforeEach
  void setUp() {
    user = UserEntity.builder()
        .userId(USER_ID)
        .email(EMAIL)
        .password(ENCODE_PASSWORD)
        .nickname(NICKNAME)
        .role(USER_ROLE)
        .build();

    signUpDTO = new SignUpDTO(
        EMAIL,
        PASSWORD,
        NICKNAME
    );

    loginDTO = new LoginDTO(
        EMAIL,
        PASSWORD
    );

    updateDTO = new UpdateDTO(
        PASSWORD,
        NEW_PASSWORD,
        NICKNAME
    );

    category = CategoryEntity.builder()
        .categoryId(CATEGORY_ID)
        .categoryName(CATEGORY_NAME)
        .user(user)
        .build();

    categoryEntities = List.of(
        CategoryEntity.builder()
            .categoryId(1L)
            .categoryName("카테고리1")
            .user(user)
            .build(),

        CategoryEntity.builder()
            .categoryId(2L)
            .categoryName("카테고리2")
            .user(user)
            .build()
    );

    categories = List.of(
        "카테고리1",
        "카테고리2"
    );

    updateCategoryDTO = new UpdateCategoryDTO(
        CATEGORY_NAME,
        NEW_CATEGORY_NAME
    );

    transactionInfoDTO = new TransactionInfoDTO(
        "지출",
        "카테고리1",
        3L
    );

    transactions = List.of(
        new Transactions(1L, "지출", "카테고리1", 1000L, "거래 내역 설명1", "거래 시간1"),
        new Transactions(2L, "지출", "카테고리1", 2000L, "거래 내역 설명2", "거래 시간2")
    );

    transactionDTO = new TransactionDTO(
        "카테고리1",
        "지출",
        1000L,
        "거래 내역 설명"
    );

    transaction = TransactionEntity.builder()
        .transactionId(TRANSACTION_ID)
        .user(user)
        .category(category)
        .type(TransactionType.getTransactionType("지출"))
        .amount(1000L)
        .description("거래 상세정보")
        .dateTime(LocalDateTime.now())
        .build();

    budgetList = List.of(
        new Budgets(BUDGET_ID, CATEGORY_NAME, 1000L, 3000L)
    );

    budgetDTO = new BudgetDTO(CATEGORY_NAME, 10000L);

    updateBudgetDTO = new UpdateBudgetDTO(BUDGET_ID, CATEGORY_NAME, 2000L);

    budget = BudgetEntity.builder()
        .budgetId(BUDGET_ID)
        .user(user)
        .category(category)
        .amount(2000L)
        .build();
  }

  // ================= User =================


  @Test
  @DisplayName("UserId 와 일치하는 사용자 정보 조회 : 성공")
  void findByUserId() {

    // given
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    // when
    UserEntity response = facadeManager.findByUserId(USER_ID);

    // then
    assertEquals(response, user);
    assertEquals(response.getUserId(), user.getUserId());
    assertEquals(response.getEmail(), user.getEmail());
    assertEquals(response.getPassword(), user.getPassword());
    assertEquals(response.getNickname(), user.getNickname());
    assertEquals(response.getRole(), user.getRole());
  }

  @Test
  @DisplayName("UserId 와 일치하는 사용자 정보 조회 : 실패")
  void findByUserId_Failed_UserNotFound() {

    // given
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.findByUserId(USER_ID));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("Reqeust Header 토큰 값으로 사용자 정보 조회 : 성공")
  void getUserByAccessToken() {

    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    // when
    UserEntity response = facadeManager.getUserByAccessToken(request);

    // then
    assertEquals(response, user);
    assertEquals(response.getUserId(), user.getUserId());
    assertEquals(response.getEmail(), user.getEmail());
    assertEquals(response.getPassword(), user.getPassword());
    assertEquals(response.getNickname(), user.getNickname());
    assertEquals(response.getRole(), user.getRole());
  }

  @Test
  @DisplayName("Reqeust Header 토큰 값으로 사용자 정보 조회 : 실패")
  void getUserByAccessTokend_Failed_UserNotFound() {

    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenThrow(
        new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.getUserByAccessToken(request));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("입력한 사용자 정보를 바탕으로 UserEntity 생성 : 성공")
  void createUser() {

    // given
    when(userRepository.existsByEmail(signUpDTO.getEmail())).thenReturn(false);
    when(userRepository.existsByNickname(signUpDTO.getNickname())).thenReturn(false);
    when(encoder.encode(signUpDTO.getPassword())).thenReturn(ENCODE_PASSWORD);

    UserEntity expectedUser = SignUpDTO.of(signUpDTO, ENCODE_PASSWORD);

    // when
    UserEntity response = facadeManager.createUser(signUpDTO);

    // then
    assertEquals(response.getEmail(), expectedUser.getEmail());
    assertEquals(response.getPassword(), expectedUser.getPassword());
    assertEquals(response.getNickname(), expectedUser.getNickname());
    assertEquals(response.getRole(), expectedUser.getRole());
  }

  @Test
  @DisplayName("입력한 사용자 정보를 바탕으로 UserEntity 생성 : 실패 - 이메일 중복")
  void createUser_Failed_ExistsEmail() {

    // given
    when(userRepository.existsByEmail(signUpDTO.getEmail())).thenReturn(true);

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.createUser(signUpDTO));

    // then
    assertEquals(
        FailedResultCode.EMAIL_ALREADY_USED.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("입력한 사용자 정보를 바탕으로 UserEntity 생성 : 실패 - 닉네임 중복")
  void createUser_Failed_Nickname() {

    // given
    when(userRepository.existsByEmail(signUpDTO.getEmail())).thenReturn(false);
    when(userRepository.existsByNickname(signUpDTO.getNickname())).thenReturn(true);

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.createUser(signUpDTO));

    // then
    assertEquals(
        FailedResultCode.NICKNAME_ALREADY_USED.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("로그인 시 입력한 사용자 정보 검증 : 통과")
  void validateLoginData() {

    // given
    when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
    when(encoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(true);

    // when
    UserEntity response = facadeManager.validateLoginData(loginDTO);

    // then
    assertEquals(response.getEmail(), user.getEmail());
    assertEquals(response.getPassword(), user.getPassword());
    assertEquals(response.getNickname(), user.getNickname());
    assertEquals(response.getRole(), user.getRole());
  }

  @Test
  @DisplayName("로그인 시 입력한 사용자 정보 검증 : 실패 - 존재하지 않은 사용자")
  void validateLoginData_Failed_UserNotFound() {

    // given
    when(userRepository.findByEmail(loginDTO.getEmail())).thenThrow(
        new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.validateLoginData(loginDTO));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("로그인 시 입력한 사용자 정보 검증 : 실패 - 비밀번호 불일치")
  void validateLoginData_Failed_Password_Mismatch() {

    // given
    when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
    when(encoder.matches(loginDTO.getPassword(), user.getPassword())).thenThrow(
        new GlobalException(FailedResultCode.PASSWORD_MISMATCH));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.validateLoginData(loginDTO));

    // then
    assertEquals(
        FailedResultCode.PASSWORD_MISMATCH.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("사용자 정보 수정 : 성공")
  void updateUserInfo() {

    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(encoder.matches(user.getPassword(), updateDTO.getBeforePassword())).thenReturn(true);
    when(userRepository.existsByNickname(updateDTO.getNickname())).thenReturn(false);
    when(encoder.encode(updateDTO.getAfterPassword())).thenReturn(ENCODE_PASSWORD);

    UserEntity expectedUser = UpdateDTO.of(user, updateDTO, ENCODE_PASSWORD);

    // when
    UserEntity response = facadeManager.updateUserInfo(request, updateDTO);

    // then
    assertEquals(response.getEmail(), expectedUser.getEmail());
    assertEquals(response.getPassword(), expectedUser.getPassword());
    assertEquals(response.getNickname(), expectedUser.getNickname());
    assertEquals(response.getRole(), expectedUser.getRole());
  }

  @Test
  @DisplayName("사용자 정보 수정 : 실패 - 존재하지 않은 사용자")
  void updateUserInfo_Failed_UserNotFound() {

    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.updateUserInfo(request, updateDTO));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("사용자 정보 수정 : 실패 - 비밀번호 불일치")
  void updateUserInfo_Failed_Password_Mismatch() {

    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(encoder.matches(user.getPassword(), updateDTO.getBeforePassword())).thenReturn(false);

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.updateUserInfo(request, updateDTO));

    // then
    assertEquals(
        FailedResultCode.PASSWORD_MISMATCH.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("사용자 정보 수정 : 실패 - 닉네임 중복")
  void updateUserInfo_Failed_ExistsNickname() {

    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(encoder.matches(user.getPassword(), updateDTO.getBeforePassword())).thenReturn(true);
    when(userRepository.existsByNickname(updateDTO.getNickname())).thenReturn(true);

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.updateUserInfo(request, updateDTO));

    // then
    assertEquals(
        FailedResultCode.NICKNAME_ALREADY_USED.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("사용자 정보 저장 : 성공")
  void saveUser() {
    // given
    when(userRepository.save(user)).thenReturn(user);

    // when
    UserEntity response = userRepository.save(user);

    // then
    assertEquals(response, user);
    assertEquals(response.getUserId(), user.getUserId());
    assertEquals(response.getEmail(), user.getEmail());
    assertEquals(response.getPassword(), user.getPassword());
    assertEquals(response.getNickname(), user.getNickname());
    assertEquals(response.getRole(), user.getRole());
  }

  @Test
  @DisplayName("사용자 정보 삭제 : 성공")
  void deleteAllUserData() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    // when
    facadeManager.deleteAllUserData(request);

    // then
    verify(budgetRepository, times(1)).deleteAllByUser(user);
    verify(transactionRepository, times(1)).deleteAllByUser(user);
    verify(categoryRepository, times(1)).deleteAllByUser(user);
    verify(userRepository, times(1)).delete(user);
  }

  @Test
  @DisplayName("사용자 정보 삭제 : 실패 - 존재하지 않은 사용자")
  void deleteAllUserData_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenThrow(
        new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.deleteAllUserData(request));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  // ================= Category =================


  @Test
  @DisplayName("User, CategoryName과 일치하는 카테고리 조회 : 성공")
  void getCategoryByUserAndCategoryName() {
    // given
    when(categoryRepository.findByUserAndCategoryName(user, CATEGORY_NAME)).thenReturn(
        Optional.of(category));

    // when
    CategoryEntity response = facadeManager.getCategoryByUserAndCategoryName(user, CATEGORY_NAME);

    // then
    assertEquals(response, category);
    assertEquals(response.getCategoryId(), category.getCategoryId());
    assertEquals(response.getCategoryName(), category.getCategoryName());
    assertEquals(response.getUser(), category.getUser());
  }

  @Test
  @DisplayName("User, CategoryName과 일치하는 카테고리 조회 : 실패 - 존재하지 않은 카테고리")
  void getCategoryByUserAndCategoryName_Failed_CategoryNotFound() {
    // given
    when(categoryRepository.findByUserAndCategoryName(user, CATEGORY_NAME))
        .thenThrow(new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.getCategoryByUserAndCategoryName(user, CATEGORY_NAME));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }


  @Test
  @DisplayName("카테고리 List 조회 : 성공")
  void getUserCategoryList() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findAllByUser(user)).thenReturn(Optional.of(categoryEntities));

    List<String> cateogries = categoryEntities.stream()
        .map(CategoryEntity::getCategoryName).toList();

    PageResponse<String> expectedResponse = PageResponse.of(categories, PAGE_NUM);

    // when
    PageResponse<String> response = facadeManager.getUserCategoryList(request, PAGE_NUM);

    // then
    assertEquals(expectedResponse.getPageNumber(), response.getPageNumber());
    assertEquals(expectedResponse.getTotalPage(), response.getTotalPage());
    assertEquals(expectedResponse.getTotalData(), response.getTotalData());
    assertEquals(expectedResponse.getDataList(), response.getDataList());
  }


  @Test
  @DisplayName("카테고리 List 조회 : 실패 - 존재하지 않은 사용자")
  void getUserCategoryList_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.getUserCategoryList(request, PAGE_NUM));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 List 조회 : 실패 - 존재하지 않은 카테고리")
  void getUserCategoryList_Failed_CategoryNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findAllByUser(user))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.getUserCategoryList(request, PAGE_NUM));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("CategoryEntity 생성 : 성공")
  void createCategory() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.existsByUserAndCategoryName(user, CATEGORY_NAME)).thenReturn(false);

    // when
    CategoryEntity response = facadeManager.createCategory(request, CATEGORY_NAME);

    // then
    assertEquals(response.getCategoryName(), category.getCategoryName());
    assertEquals(response.getUser(), category.getUser());
  }

  @Test
  @DisplayName("CategoryEntity 생성 : 실패 - 존재하지 않은 사용자")
  void createCategory_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.createCategory(request, CATEGORY_NAME));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("CategoryEntity 생성 : 실패 - 이미 등록한 카테고리")
  void createCategory_Failed_CategoryExists() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.existsByUserAndCategoryName(user, CATEGORY_NAME))
        .thenThrow(new GlobalException(FailedResultCode.CATEGORY_ALREADY_USED));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.createCategory(request, CATEGORY_NAME));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_ALREADY_USED.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 수정 : 성공")
  void updateCategory() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user,
        updateCategoryDTO.getBeforecategoryName())).thenReturn(Optional.of(category));
    when(categoryRepository.existsByUserAndCategoryName(user,
        updateCategoryDTO.getAfterCategoryName())).thenReturn(false);

    CategoryEntity expectedCategory = UpdateCategoryDTO.of(category, updateCategoryDTO);

    // when
    CategoryEntity response = facadeManager.updateCategory(request, updateCategoryDTO);

    // then
    assertEquals(response.getCategoryName(), expectedCategory.getCategoryName());
    assertEquals(response.getUser(), expectedCategory.getUser());
  }

  @Test
  @DisplayName("카테고리 수정 : 실패 - 존재하지 않은 사용자")
  void updateCategory_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.updateCategory(request, updateCategoryDTO));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 수정 : 실패 - 존재하지 않은 카테고리")
  void updateCategory_Failed_CategoryNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user,
        updateCategoryDTO.getBeforecategoryName())).thenThrow(
        new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.updateCategory(request, updateCategoryDTO));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 수정 : 실패 - 이미 등록한 카테고리")
  void updateCategory_Failed_CategoryExists() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user,
        updateCategoryDTO.getBeforecategoryName())).thenReturn(Optional.of(category));
    when(categoryRepository.existsByUserAndCategoryName(user,
        updateCategoryDTO.getAfterCategoryName())).thenReturn(true);

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.updateCategory(request, updateCategoryDTO));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_ALREADY_USED.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 저장 : 성공")
  void saveCategory() {
    // given
    when(categoryRepository.save(category)).thenReturn(category);

    // when
    CategoryEntity response = categoryRepository.save(category);

    // then
    assertEquals(response.getCategoryName(), category.getCategoryName());
    assertEquals(response.getUser(), category.getUser());
  }

  @Test
  @DisplayName("카테고리 삭제 : 성공")
  void deleteCategory() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user, CATEGORY_NAME))
        .thenReturn(Optional.of(category));

    // when
    facadeManager.deleteCategory(request, CATEGORY_NAME);

    // then
    verify(categoryRepository, times(1)).delete(category);
  }

  @Test
  @DisplayName("카테고리 삭제 : 실패 - 존재하지 않은 사용자")
  void deleteCategory_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.deleteCategory(request, CATEGORY_NAME));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("카테고리 삭제 : 실패 - 존재하지 않은 카테고리")
  void deleteCategory_Failed_CategoryNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user,
        updateCategoryDTO.getBeforecategoryName())).thenThrow(
        new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.deleteCategory(request, CATEGORY_NAME));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  // ================= Trnsaction =================


  @Test
  @DisplayName("거래 정보 List 조회 : 성공")
  void getTransactionList() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(transactionQueryRepository.getTransactionInfo(user, transactionInfoDTO))
        .thenReturn(transactions);

    PageResponse<Transactions> expectedResponse = PageResponse.of(transactions, PAGE_NUM);

    // when
    PageResponse<Transactions> response = facadeManager
        .getTransactionList(request, transactionInfoDTO, PAGE_NUM);

    // then
    assertEquals(expectedResponse.getPageNumber(), response.getPageNumber());
    assertEquals(expectedResponse.getTotalPage(), response.getTotalPage());
    assertEquals(expectedResponse.getTotalData(), response.getTotalData());
    assertEquals(expectedResponse.getDataList(), response.getDataList());
  }

  @Test
  @DisplayName("거래 정보 List 조회 : 실패 - 존재하지 않은 사용자")
  void getTransactionList_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.getTransactionList(request, transactionInfoDTO, PAGE_NUM));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("TransactionEntity 생성 : 성공")
  void createTransaction() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user, transactionDTO.getCategoryName()))
        .thenReturn(Optional.of(category));

    TransactionEntity expectedTransaction = TransactionDTO.of(user, category, transactionDTO);

    // when
    TransactionEntity response = facadeManager.createTransaction(request, transactionDTO);

    // then
    assertEquals(expectedTransaction.getUser(), response.getUser());
    assertEquals(expectedTransaction.getCategory(), response.getCategory());
    assertEquals(expectedTransaction.getType(), response.getType());
    assertEquals(expectedTransaction.getAmount(), response.getAmount());
    assertEquals(expectedTransaction.getDescription(), response.getDescription());
  }

  @Test
  @DisplayName("TransactionEntity 생성 : 실패 존재 하지 않은 사용자")
  void createTransaction_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.createTransaction(request, transactionDTO));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("TransactionEntity 생성 : 실패 - 존재하지 않은 카테고리")
  void createTransaction_Failed_CategoryNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user,
        transactionDTO.getCategoryName())).thenThrow(
        new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.createTransaction(request, transactionDTO));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("TransactionEntity 저장 : 성공")
  void saveTransaction() {
    // given
    when(transactionRepository.save(transaction)).thenReturn(transaction);

    // when
    TransactionEntity response = transactionRepository.save(transaction);

    // then
    assertEquals(response.getUser(), transaction.getUser());
    assertEquals(response.getCategory(), transaction.getCategory());
    assertEquals(response.getType(), transaction.getType());
    assertEquals(response.getAmount(), transaction.getAmount());
    assertEquals(response.getDescription(), transaction.getDescription());
  }

  @Test
  @DisplayName("TransactionEntity 삭제 : 성공")
  void deleteTransaction() {
    // given
    when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transaction));

    // when
    facadeManager.deleteTransaction(TRANSACTION_ID);

    // then
    verify(transactionRepository, times(1)).delete(transaction);
  }

  @Test
  @DisplayName("TransactionEntity 삭제 : 실패 - 존재하지 않은 거래 내역")
  void deleteTransaction_Failed_TransactionNotFound() {
    // given
    when(transactionRepository.findById(TRANSACTION_ID))
        .thenThrow(new GlobalException(FailedResultCode.TRANSACTION_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.deleteTransaction(TRANSACTION_ID));

    // then
    assertEquals(
        FailedResultCode.TRANSACTION_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  // ================= Budget =================

  @Test
  @DisplayName("예산 List 조회 : 성공")
  void getBudgetList() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(budgetQueryRepository.getBudgetList(user, CATEGORY_NAME)).thenReturn(budgetList);

    PageResponse<Budgets> expectedResponse = PageResponse.of(budgetList, PAGE_NUM);

    // when
    PageResponse<Budgets> response = facadeManager.getBudgetList(request, CATEGORY_NAME, PAGE_NUM);

    // then
    assertEquals(expectedResponse.getPageNumber(), response.getPageNumber());
    assertEquals(expectedResponse.getTotalPage(), response.getTotalPage());
    assertEquals(expectedResponse.getTotalData(), response.getTotalData());
    assertEquals(expectedResponse.getDataList(), response.getDataList());
  }

  @Test
  @DisplayName("예산 List 조회 : 실패 존재 하지 않은 사용자")
  void getBudgetList_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.getBudgetList(request, CATEGORY_NAME, PAGE_NUM));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("BudgetEntity 생성 : 성공")
  void createBudget() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user, budgetDTO.getCategoryName()))
        .thenReturn(Optional.of(category));

    BudgetEntity expectedBudget = BudgetDTO.of(user, category, budgetDTO);

    // when
    BudgetEntity response = facadeManager.createBudget(request, budgetDTO);

    // then
    assertEquals(expectedBudget.getUser(), response.getUser());
    assertEquals(expectedBudget.getCategory(), response.getCategory());
    assertEquals(expectedBudget.getAmount(), response.getAmount());
  }

  @Test
  @DisplayName("BudgetEntity 생성 : 실패 존재 하지 않은 사용자")
  void createBudget_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.createBudget(request, budgetDTO));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("BudgetEntity 생성 : 실패 - 존재하지 않은 카테고리")
  void createBudget_Failed_CategoryNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user,
        budgetDTO.getCategoryName())).thenThrow(
        new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.createBudget(request, budgetDTO));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("BudgetEntity 수정 : 성공")
  void updateBudget() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user, updateBudgetDTO.getCategoryName()))
        .thenReturn(Optional.of(category));

    BudgetEntity expectedBudget = UpdateBudgetDTO.of(user, category, updateBudgetDTO);

    // when
    BudgetEntity response = facadeManager.updateBudget(request, updateBudgetDTO);

    // then
    assertEquals(expectedBudget.getUser(), response.getUser());
    assertEquals(expectedBudget.getCategory(), response.getCategory());
    assertEquals(expectedBudget.getAmount(), response.getAmount());
  }

  @Test
  @DisplayName("BudgetEntity 수정 : 실패 존재 하지 않은 사용자")
  void updateBudget_Failed_UserNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID))
        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.updateBudget(request, updateBudgetDTO));

    // then
    assertEquals(
        FailedResultCode.USER_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("BudgetEntity 수정 : 실패 - 존재하지 않은 카테고리")
  void updateBudget_Failed_CategoryNotFound() {
    // given
    when(jwtUtil.getUserId(request)).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(categoryRepository.findByUserAndCategoryName(user,
        updateBudgetDTO.getCategoryName())).thenThrow(
        new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.updateBudget(request, updateBudgetDTO));

    // then
    assertEquals(
        FailedResultCode.CATEGORY_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }

  @Test
  @DisplayName("BudgetEntity 저장 : 성공")
  void saveBudget() {
    // given
    when(budgetRepository.save(budget)).thenReturn(budget);

    // when
    BudgetEntity response = budgetRepository.save(budget);

    // then
    assertEquals(response.getUser(), budget.getUser());
    assertEquals(response.getCategory(), budget.getCategory());
    assertEquals(response.getAmount(), budget.getAmount());
  }

  @Test
  @DisplayName("BudgetEntity 삭제 : 성공")
  void deleteBudget() {
    // given
    when(budgetRepository.findById(BUDGET_ID)).thenReturn(Optional.of(budget));

    // when
    facadeManager.deleteBudget(BUDGET_ID);

    // then
    verify(budgetRepository, times(1)).delete(budget);
  }

  @Test
  @DisplayName("BudgetEntity 삭제 : 실패 - 존재하지 않은 예산 정보")
  void deleteBudget_Failed_BudgetNotFound() {
    // given
    when(budgetRepository.findById(BUDGET_ID))
        .thenThrow(new GlobalException(FailedResultCode.BUDGET_NOT_FOUND));

    // when
    GlobalException exception = assertThrows(GlobalException.class,
        () -> facadeManager.deleteBudget(BUDGET_ID));

    // then
    assertEquals(
        FailedResultCode.BUDGET_NOT_FOUND.getStatus(),
        exception.getResultResponse().getStatus()
    );
  }
}