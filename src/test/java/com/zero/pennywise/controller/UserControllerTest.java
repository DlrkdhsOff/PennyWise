package com.zero.pennywise.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.pennywise.PennyWiseApplication;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.account.RegisterDTO;
import com.zero.pennywise.model.dto.account.UpdateDTO;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = PennyWiseApplication.class)
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;


  private RegisterDTO createRegisterDTO(String email, String password, String username,
      String phone) {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setEmail(email);
    registerDTO.setPassword(password);
    registerDTO.setUsername(username);
    registerDTO.setPhone(phone);
    return registerDTO;
  }

  private UpdateDTO createUpdateDTO(String password, String username,
      String phone) {
    UpdateDTO  updateDTO = new UpdateDTO();
    updateDTO.setPassword(password);
    updateDTO.setUsername(username);
    updateDTO.setPhone(phone);
    return updateDTO;
  }


  // 회원 가입 성공
  @Test
  @Transactional
  void testRegister_Success() throws Exception {
    // Given
    RegisterDTO registerDTO = createRegisterDTO("newUser@example.com", "1111", "user1", "01012345678");

    // When
    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))

    // Then
        .andExpect(status().isCreated())
        .andExpect(content().string("회원가입 성공"));
  }

  // 회원 가입 실패 : 존재하는 아이디
  @Test
  @Transactional
  void testRegister_DuplicateEmail() {
    // Given
    String existingEmail = "existingUser@example.com";
    RegisterDTO existingUserDTO = createRegisterDTO(existingEmail, "1111", "user1", "01012345678");
    userService.register(existingUserDTO);

    // When
    RegisterDTO duplicateUserDTO = createRegisterDTO(existingEmail, "2222", "user2", "01087654321");

    GlobalException exception = assertThrows(GlobalException.class, () -> {
      userService.register(duplicateUserDTO);
    });

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("이미 존재하는 아이디 입니다.", exception.getMessage());
  }

  // 회원 가입 실패 : 유효하지 않은 문자 포함
  @Test
  void testRegister_InvalidPhoneCharacters() throws Exception {

    // Given
    RegisterDTO registerDTO = createRegisterDTO("validUser@example.com", "1111", "user1",
        "010-1234-5678");

    // When
    GlobalException exception = assertThrows(GlobalException.class, () -> {
      userService.register(registerDTO);
    });

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("전화번호에 유효하지 않은 문자가 포함되어 있습니다.", exception.getMessage());
  }

  // 회원 가입 실패 : 유효하지 않은 전화번호 길이
  @Test
  void testRegister_InvalidPhoneLength() throws Exception {
    // Given
    RegisterDTO registerDTO = createRegisterDTO("validUser@example.com", "1111", "user1",
        "0101234567810");

    // When
    GlobalException exception = assertThrows(GlobalException.class, () -> {
      userService.register(registerDTO);
    });

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("유효하지 않은 전화번호 입니다.", exception.getMessage());
  }

  // 회원 정보 수정 성공
  @Test
  @Transactional
  void testUpdate_Success() throws Exception {

    // Given
    RegisterDTO registerDTO = createRegisterDTO("updateUser@example.com", "1111", "user1",
        "01012345678");

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isCreated())
        .andExpect(content().string("회원가입 성공"));

    UserEntity user = userRepository.findByEmail("updateUser@example.com");
    UpdateDTO updateDTO = createUpdateDTO("newPassword", "newUser1", "01098765432");

    // When
    mockMvc.perform(patch("/account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO))
            .sessionAttr("userId", user.getId()))

    // Then
        .andExpect(status().isOk())
        .andExpect(content().string("회원 정보가 성공적으로 수정되었습니다."));
  }

  // 회원 정보 수정 실패 : 유효하지 않은 문자 포함
  @Test
  @Transactional
  void testUpdate_InvalidPhoneCharacters() throws Exception {

    // Given
    RegisterDTO registerDTO = createRegisterDTO("userForUpdate@example.com", "1111", "user1",
        "01012345678");

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isCreated())
        .andExpect(content().string("회원가입 성공"));

    UserEntity user = userRepository.findByEmail("userForUpdate@example.com");
    UpdateDTO updateDTO = createUpdateDTO("newPassword", "newUser1", "010-1234-5678");

    // When
    GlobalException exception = assertThrows(GlobalException.class, () -> {
      userService.update(user.getId(), updateDTO);
    });

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("전화번호에 유효하지 않은 문자가 포함되어 있습니다.", exception.getMessage());
  }

  // 회원 정보 수정 실패 : 유효하지 않은 전화번호 길이
  @Test
  @Transactional
  void testUpdate_InvalidPhoneLength() throws Exception {
    // Given
    RegisterDTO registerDTO = createRegisterDTO("userForUpdate@example.com", "1111", "user1",
        "01012345678");

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isCreated())
        .andExpect(content().string("회원가입 성공"));

    UserEntity user = userRepository.findByEmail("userForUpdate@example.com");
    UpdateDTO updateDTO = createUpdateDTO("newPassword", "newUser1", "010123456789");

    // When
    GlobalException exception = assertThrows(GlobalException.class, () -> {
      userService.update(user.getId(), updateDTO);
    });

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("유효하지 않은 전화번호 입니다.", exception.getMessage());
  }

  // 회원 정보 수정 실패 : 매개변수가 전부 빈 값인 경우
  @Test
  @Transactional
  void testUpdate_ParameterIsNull() throws Exception {
    // Given
    RegisterDTO registerDTO = createRegisterDTO("userForUpdate@example.com", "1111", "user1",
        "01012345678");

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isCreated())
        .andExpect(content().string("회원가입 성공"));

    UserEntity user = userRepository.findByEmail("userForUpdate@example.com");
    UpdateDTO updateDTO = createUpdateDTO("", "", "");

    // When
    GlobalException exception = assertThrows(GlobalException.class, () -> {
      userService.update(user.getId(), updateDTO);
    });

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("회원 정보 수정에 실패 했습니다.", exception.getMessage());
  }


  // 회원 탈퇴 성공
  @Test
  @Transactional
  void testDelete_Success() throws Exception {
    // Given
    RegisterDTO registerDTO = createRegisterDTO("userForUpdate@example.com", "1111", "user1",
        "01012345678");

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isCreated())
        .andExpect(content().string("회원가입 성공"));

    UserEntity user = userRepository.findByEmail("userForUpdate@example.com");

    // When
    mockMvc.perform(delete("/account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user.getId()))
            .sessionAttr("userId", user.getId()))

        // Then
        .andExpect(status().isOk())
        .andExpect(content().string("계정이 영구적으로 삭제 되었습니다."));
  }

}


