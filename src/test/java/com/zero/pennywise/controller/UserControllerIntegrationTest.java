//package com.zero.pennywise.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zero.pennywise.PennyWiseApplication;
//import com.zero.pennywise.model.dto.RegisterDTO;
//import com.zero.pennywise.model.dto.UpdateDTO;
//import com.zero.pennywise.model.entity.UserEntity;
//import com.zero.pennywise.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest(classes = PennyWiseApplication.class)
//@AutoConfigureMockMvc
//class UserControllerIntegrationTest {
//
//  private static final String REGISTER_SUCCESS_MSG = "회원가입 성공";
//  private static final String DUPLICATE_ID_MSG = "중복된 아이디 입니다.";
//  private static final String INVALID_PHONE_CHAR_MSG = "전화번호에 유효하지 않은 문자가 포함되어 있습니다.";
//  private static final String INVALID_PHONE_LENGTH_MSG = "유효하지 않은 전화번호 입니다.";
//  private static final String UPDATE_SUCCESS_MSG = "회원 정보가 성공적으로 수정되었습니다.";
//  private static final String PARAMETER_IS_NULL_MSG = "회원 정보 수정에 실패 했습니다.";
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @Autowired
//  private ObjectMapper objectMapper;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  private RegisterDTO createRegisterDTO(String email, String password, String username,
//      String phone) {
//    RegisterDTO registerDTO = new RegisterDTO();
//    registerDTO.setEmail(email);
//    registerDTO.setPassword(password);
//    registerDTO.setUsername(username);
//    registerDTO.setPhone(phone);
//    return registerDTO;
//  }
//
//  private UpdateDTO createUpdateDTO(String password, String username, String phone) {
//    UpdateDTO updateDTO = new UpdateDTO();
//    updateDTO.setPassword(password);
//    updateDTO.setUsername(username);
//    updateDTO.setPhone(phone);
//    return updateDTO;
//  }
//
//  // 회원 가입 성공
//  @Test
//  @Transactional
//  void testRegister_Success() throws Exception {
//    RegisterDTO registerDTO = createRegisterDTO("newUser@example.com", "1111", "user1",
//        "01012345678");
//
//    mockMvc.perform(post("/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerDTO)))
//        .andExpect(status().isCreated())
//        .andExpect(content().string(REGISTER_SUCCESS_MSG));
//  }
//
//  // 회원 가입 실패 : 중복된 아이디
//  @Test
//  @Transactional
//  void testRegister_DuplicateId() throws Exception {
//    RegisterDTO registerDTO = createRegisterDTO("duplicateUser@example.com", "1111", "user1",
//        "01012345678");
//
//    mockMvc.perform(post("/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerDTO)))
//        .andExpect(status().isCreated())
//        .andExpect(content().string(REGISTER_SUCCESS_MSG));
//
//    mockMvc.perform(post("/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerDTO)))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(DUPLICATE_ID_MSG));
//  }
//
//  // 회원 가입 실패 : 유효하지 않은 문자 포함
//  @Test
//  void testRegister_InvalidPhoneCharacters() throws Exception {
//    RegisterDTO registerDTO = createRegisterDTO("validUser@example.com", "1111", "user1",
//        "010-1234-5678");
//
//    mockMvc.perform(post("/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerDTO)))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(INVALID_PHONE_CHAR_MSG));
//  }
//
//  // 회원 가입 실패 : 유효하지 않은 전화번호 길이
//  @Test
//  void testRegister_InvalidPhoneLength() throws Exception {
//    RegisterDTO registerDTO = createRegisterDTO("validUser@example.com", "1111", "user1",
//        "0101234567890");
//
//    mockMvc.perform(post("/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerDTO)))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(INVALID_PHONE_LENGTH_MSG));
//  }
//
//  // 회원 정보 수정 성공
//  @Test
//  @Transactional
//  void testUpdate_Success() throws Exception {
//    RegisterDTO registerDTO = createRegisterDTO("updateUser@example.com", "1111", "user1",
//        "01012345678");
//
//    mockMvc.perform(post("/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerDTO)))
//        .andExpect(status().isCreated())
//        .andExpect(content().string(REGISTER_SUCCESS_MSG));
//
//    UserEntity user = userRepository.findByEmail("updateUser@example.com");
//
//    UpdateDTO updateDTO = createUpdateDTO("newPassword", "newUser1", "01098765432");
//
//    mockMvc.perform(patch("/update")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(updateDTO))
//            .sessionAttr("userId", user.getId()))
//        .andExpect(status().isOk())
//        .andExpect(content().string(UPDATE_SUCCESS_MSG));
//  }
//
//  // 회원 정보 수정 실패 : 유효하지 않은 문자 포함
//  @Test
//  @Transactional
//  void testUpdate_InvalidPhoneCharacters() throws Exception {
//    RegisterDTO registerDTO = createRegisterDTO("userForUpdate@example.com", "1111", "user1",
//        "01012345678");
//
//    mockMvc.perform(post("/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerDTO)))
//        .andExpect(status().isCreated())
//        .andExpect(content().string(REGISTER_SUCCESS_MSG));
//
//    UserEntity user = userRepository.findByEmail("userForUpdate@example.com");
//
//    UpdateDTO updateDTO = createUpdateDTO("newPassword", "newUser1", "010-1234-5678");
//
//    mockMvc.perform(patch("/update")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(updateDTO))
//            .sessionAttr("userId", user.getId()))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(INVALID_PHONE_CHAR_MSG));
//  }
//
//  // 회원 정보 수정 실패 : 유효하지 않은 전화번호 길이
//  @Test
//  @Transactional
//  void testUpdate_InvalidPhoneLength() throws Exception {
//    RegisterDTO registerDTO = createRegisterDTO("userForUpdate@example.com", "1111", "user1",
//        "01012345678");
//
//    mockMvc.perform(post("/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerDTO)))
//        .andExpect(status().isCreated())
//        .andExpect(content().string(REGISTER_SUCCESS_MSG));
//
//    UserEntity user = userRepository.findByEmail("userForUpdate@example.com");
//
//    UpdateDTO updateDTO = createUpdateDTO("newPassword", "newUser1", "010123456789");
//
//    mockMvc.perform(patch("/update")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(updateDTO))
//            .sessionAttr("userId", user.getId()))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(INVALID_PHONE_LENGTH_MSG));
//  }
//
//  // 회원 정보 수정 실패 : 매개변수가 전부 빈 값인 경우
//  @Test
//  @Transactional
//  void testUpdate_ParameterIsNull() throws Exception {
//    // 사용자 등록
//    RegisterDTO registerDTO = createRegisterDTO("userForUpdate@example.com", "1111", "user1",
//        "01012345678");
//
//    // 회원가입 요청
//    mockMvc.perform(post("/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerDTO)))
//        .andExpect(status().isCreated())
//        .andExpect(content().string(REGISTER_SUCCESS_MSG));
//
//    UserEntity user = userRepository.findByEmail("userForUpdate@example.com");
//
//    // UpdateDTO 생성
//    UpdateDTO updateDTO = createUpdateDTO("", "", "");
//
//    // 회원 정보 수정 요청
//    mockMvc.perform(patch("/update")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(updateDTO))
//            .sessionAttr("userId", user.getId()))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(PARAMETER_IS_NULL_MSG));
//  }
//}