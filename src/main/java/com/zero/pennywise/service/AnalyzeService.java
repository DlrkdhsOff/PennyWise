//package com.zero.pennywise.service;
//
//import com.zero.pennywise.component.handler.AnalyzeHandler;
//import com.zero.pennywise.component.handler.UserHandler;
//import com.zero.pennywise.entity.UserEntity;
//import com.zero.pennywise.enums.AnalyzeMessage;
//import com.zero.pennywise.model.response.analyze.AnalyzeDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AnalyzeService {
//
//  private final AnalyzeHandler analyzeHandler;
//  private final UserHandler userHandler;
//
//
//
//  public String analyze(Long userId) {
//    UserEntity user = userHandler.getUserById(userId);
//
//    // 지난 3달 지출 금액
//    AnalyzeDTO lastThreeMonth = analyzeHandler.getLastThreeMonthBalance(userId);
//
//    // 이번달 지출 금액
//    AnalyzeDTO thisMonth = analyzeHandler.getThisMonthBalance(userId);
//
//    Long lastTotalAmount = lastThreeMonth.getTotalExpenses();
//    Long thisMonthAmount = thisMonth.getTotalExpenses();
//
//    analyzeHandler.isExpensesToHigh(user, lastTotalAmount, thisMonthAmount, thisMonth);
//
//    return AnalyzeMessage.getMessage(lastTotalAmount, thisMonthAmount, lastThreeMonth, thisMonth);
//  }
//
//
//}
//
