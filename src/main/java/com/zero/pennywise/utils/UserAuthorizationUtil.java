package com.zero.pennywise.utils;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.user.UserDetailsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthorizationUtil {

  public static Long getLoginUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

    if (userDetails == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return userDetails.getUserId();
  }

}
