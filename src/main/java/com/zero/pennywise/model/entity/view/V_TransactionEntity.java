package com.zero.pennywise.model.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "v_transaction")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class V_TransactionEntity {

  @Id
  private Long transactionId;

  private Long userId;
  private String type;
  private String categoryName;
  private Long amount;
  private String description;
  private LocalDateTime date;
  private boolean isFixed;


}
