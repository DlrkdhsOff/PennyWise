package com.zero.pennywise.service;

import com.zero.pennywise.entity.WaringMessageEntity;
import com.zero.pennywise.model.response.waring.WaringMessageDTO;
import com.zero.pennywise.model.response.waring.WaringMessagePage;
import com.zero.pennywise.repository.WaringMessageRepository;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaringMessageService {

  private final WaringMessageRepository waringMessageRepository;

  public WaringMessagePage getWaringMessage(Long userId, Pageable page) {
    Page<WaringMessageEntity> list = waringMessageRepository.findAllByUserId(userId, page);

    return WaringMessagePage.of(list.map(this::convertToDto));
  }

  private WaringMessageDTO convertToDto(WaringMessageEntity waringMessage) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");

    return new WaringMessageDTO(waringMessage.getMessage(), waringMessage.getRecivedDateTime().format(formatter));
  }
}
