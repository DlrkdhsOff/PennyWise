//package com.zero.pennywise.controller;
//
//import static com.zero.pennywise.utils.PageUtils.page;
//
//import com.zero.pennywise.model.request.savings.DeleteSavingsDTO;
//import com.zero.pennywise.model.request.savings.SavingsDTO;
//import com.zero.pennywise.service.SavingsService;
//import com.zero.pennywise.utils.UserAuthorizationUtil;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1")
//public class SavingsController {
//
//  private final SavingsService savingsService;
//
//  @PostMapping("/savings")
//  public ResponseEntity<?> setSavings(@RequestBody @Valid SavingsDTO savings) {
//
//    Long userId = UserAuthorizationUtil.getLoginUserId();
//
//    return ResponseEntity.ok()
//        .body(savingsService.setSavings(userId, savings));
//  }
//
//  @GetMapping("/savings")
//  public ResponseEntity<?> savings(@PageableDefault(page = 1, size = 11) Pageable page) {
//
//    Long userId = UserAuthorizationUtil.getLoginUserId();
//
//    return ResponseEntity.ok().body(savingsService.getSavings(userId, page(page)));
//  }
//
//  @DeleteMapping("/savings")
//  public ResponseEntity<?> deleteSavings(@RequestBody @Valid DeleteSavingsDTO deleteSavingsDTO) {
//
//    Long userId = UserAuthorizationUtil.getLoginUserId();
//
//    return ResponseEntity.ok().body(savingsService.deleteSavings(userId, deleteSavingsDTO));
//  }
//
//  @GetMapping("/recommend")
//  public ResponseEntity<?> recommend() {
//    Long userId = UserAuthorizationUtil.getLoginUserId();
//
//    return ResponseEntity.ok().body(savingsService.recommend(userId));
//  }
//}
