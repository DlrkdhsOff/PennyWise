//package com.zero.pennywise.model.dto;
//
//import static com.zero.pennywise.status.TransactionStatus.castToTransactionStatus;
//
//import com.zero.pennywise.model.entity.TransactionEntity;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import lombok.Data;
//
//@Data
//public class TransactionDTO {
//
//  @NotBlank(message = "카테고리명을 입력해주세요.")
//  private String categoryName;
//
//  @NotBlank(message = "지출/수입을 입력해주세요.")
//  private String type;
//
//  @NotNull(message = "금액을 입력해주세요.")
//  private Long amount;
//
//  private String description;
//
//  @NotBlank(message = "고정적인 지출/수입일 경우 \"Y\"를 입력해주세요.")
//  private String isFixed;
//
//  public static TransactionEntity of(Long userId, Long categoryId, TransactionDTO transactionDTO) {
//    return TransactionEntity.builder()
//        .userId(userId)
//        .categoryId(categoryId)
//        .type(castToTransactionStatus(transactionDTO.getType(), transactionDTO.getIsFixed()))
//        .amount(transactionDTO.getAmount())
//        .description(transactionDTO.getDescription())
//        .dateTime(dateTimeFormatting(LocalDateTime.now()))
//        .build();
//  }
//
//   // "yyyy-MM-dd HH:mm:ss" 형식
//    public static String dateTimeFormatting(LocalDateTime date) {
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    return date.format(formatter);
//  }
//
//}
