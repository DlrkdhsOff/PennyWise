package com.zero.pennywise.service;

import com.zero.pennywise.model.dto.LoginDTO;
import com.zero.pennywise.model.dto.RegisterDTO;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.status.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Response register(RegisterDTO registerDTO) {

    if (userRepository.existsByUserId(registerDTO.getUserId())) {
      return new Response(AccountStatus.REGISTER_FAILED_DUPLICATE_ID);
    }

    if (!registerDTO.getPhone().matches("^[0-9]+$")) {
      return new Response(AccountStatus.PHONE_NUMBER_CONTAINS_INVALID_CHARACTERS);
    } else if (registerDTO.getPhone().length() > 11) {
      return new Response(AccountStatus.PHONE_NUMBER_INVALID);
    }

    UserEntity userEntity = RegisterDTO.of(registerDTO);
    userRepository.save(userEntity);

    return new Response(AccountStatus.REGISTER_SUCCESS);
  }

  public Response login(LoginDTO loginDTO) {
    if (!userRepository.existsByUserId(loginDTO.getUserId())) {
      return new Response(AccountStatus.USER_NOT_FOUND);
    }

    UserEntity user = userRepository.findByUserId(loginDTO.getUserId());

    if (!user.getPassword().equals(loginDTO.getPassword())) {
      return new Response(AccountStatus.PASSWORD_DOES_NOT_MATCH);
    }

    return new Response(AccountStatus.LOGIN_SUCCESS);
  }
}
