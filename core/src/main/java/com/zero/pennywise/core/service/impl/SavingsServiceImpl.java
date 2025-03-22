//package com.zero.pennywise.core.service.impl;
//
//
//import com.zero.pennywise.component.SavingHandler;
//import com.zero.pennywise.entity.UserEntity;
//import com.zero.pennywise.model.response.ResultResponse;
//import com.zero.pennywise.model.response.balances.Balances;
//import com.zero.pennywise.model.response.savings.RecommendSavings;
//import com.zero.pennywise.model.type.SuccessResultCode;
//import com.zero.pennywise.model.type.TokenType;
//import com.zero.pennywise.api.service.SavingsService;
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class SavingsServiceImpl implements SavingsService {
//
//  private final JwtUtil jwtUtil;
//  private final UserHandler userHandler;
//  private final SavingHandler savingHandler;
//
//  // 요청 헤더에서 사용자 정보를 추출하여 반환
//  private UserEntity fetchUser(HttpServletRequest request) {
//    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
//    return userHandler.findByUserId(userId);
//  }
//
//  @Override
//  public ResultResponse recommend(HttpServletRequest request) {
//    UserEntity user = fetchUser(request);
//
//    List<Balances> recentAvgList = savingHandler.getRecentAvg(user);
//
//    RecommendSavings recommend = savingHandler.recommend(recentAvgList);
//
//    return new ResultResponse(SuccessResultCode.SUCCESS_GET_CATEGORY_LIST, recommend);
//  }
//}
