package com.zero.pennywise.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.pennywise.PennyWiseApplication;
import com.zero.pennywise.model.dto.RegisterDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = PennyWiseApplication.class)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Transactional
  void testRegister_Success() throws Exception {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setEmail("newUser");
    registerDTO.setPassword("1111");
    registerDTO.setUsername("user1");
    registerDTO.setPhone("01012345678");

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isCreated())
        .andExpect(content().string("회원가입 성공"));
  }

  @Test
  @Transactional
  void testRegister_DuplicateId() throws Exception {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setEmail("duplicateUser");
    registerDTO.setPassword("1111");
    registerDTO.setUsername("user1");
    registerDTO.setPhone("01012345678");

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isCreated())
        .andExpect(content().string("회원가입 성공"));

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("중복된 아이디 입니다."));
  }

  @Test
  void testRegister_InvalidPhoneCharacters() throws Exception {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setEmail("validUser");
    registerDTO.setPassword("1111");
    registerDTO.setUsername("user1");
    registerDTO.setPhone("010-1234-5678");

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("전화번호에 유효하지 않은 문자가 포함되어 있습니다."));
  }

  @Test
  void testRegister_InvalidPhoneLength() throws Exception {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setEmail("validUser");
    registerDTO.setPassword("1111");
    registerDTO.setUsername("user1");
    registerDTO.setPhone("0101234567890");

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("유효하지 않은 전화번호 입니다."));
  }
}