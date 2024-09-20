package com.zero.pennywise.controller;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.model.response.waring.WaringMessagePage;
import com.zero.pennywise.service.WaringMessageService;
import com.zero.pennywise.utils.UserAuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WaringMessageController {

  private final WaringMessageService waringMessageService;

  // 거래 목록 출력 (전체 / 카테고리별)
  @GetMapping("/waring-message")
  public ResponseEntity<WaringMessagePage> getWaringMessageList(@PageableDefault(page = 0, size = 10) Pageable page)
  {
    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.ok()
        .body(waringMessageService.getWaringMessage(userId, page(page)));
  }
}
