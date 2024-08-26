package com.zero.pennywise.repository.view;

import com.zero.pennywise.model.entity.view.V_TransactionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface V_TransactionRepository extends JpaRepository<V_TransactionEntity, Long> {

  List<V_TransactionEntity> findAllByUserIdAndCategoryName(Long userId, String categoryName);

  List<V_TransactionEntity> findAllByUserId(Long userId);
}
