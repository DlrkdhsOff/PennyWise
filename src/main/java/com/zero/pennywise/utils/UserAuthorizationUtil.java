package com.zero.pennywise.utils;

import com.zero.pennywise.model.request.account.UserDetailsDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthorizationUtil {

  public static Long getLoginUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

    return userDetails.getUserId();
  }

}
