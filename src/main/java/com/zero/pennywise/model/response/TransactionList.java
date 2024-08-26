package com.zero.pennywise.model.response;

import com.zero.pennywise.model.entity.view.V_TransactionEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TransactionList {

  private String type;
  private String categoryName;
  private Long amount;
  private String description;
  private String date;

  public static List<TransactionList> of(List<V_TransactionEntity> transactionList) {
    List<TransactionList> list = new ArrayList<>();

    for (V_TransactionEntity transaction : transactionList) {
      TransactionList t = new TransactionList();
      t.setType(transaction.isFixed()?"고정 " + transaction.getType(): transaction.getType());
      t.setCategoryName(transaction.getCategoryName());
      t.setAmount(transaction.getAmount());
      t.setDescription(transaction.getDescription());
      t.setDate(dateTimeFormatting(transaction.getDate()));
      list.add(t);
    }

    return list;
  }

  public static String dateTimeFormatting(LocalDateTime date) {

    // "yyyy-MM-dd HH:mm:ss" 형식
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    return date.format(formatter);
  }


}
