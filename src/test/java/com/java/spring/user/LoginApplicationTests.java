package com.java.spring.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.java.spring.dto.LoginUserDto;
import com.java.spring.model.User;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;
import com.java.spring.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class LoginApplicationTests {


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  UserRepository repository;

  @Autowired
  PostRepository postRepository;

  @Autowired
  CategoriesRepository categoriesRepository;

  @BeforeEach
  void setUp() throws Exception {
    postRepository.deleteAll();
    categoriesRepository.deleteAll();
    repository.deleteAll();
  }

  @AfterEach
  void afterEach() throws Exception {
    postRepository.deleteAll();
    categoriesRepository.deleteAll();
    repository.deleteAll();
  }

  @Test
  @Order(1)
  @DisplayName("1 - must login successfully")
  void loginSuccessfully() throws Exception {
    User newUser = new User();
    newUser.setDisplayName("Usuário de teste");
    newUser.setEmail("email_teste@email.com");
    newUser.setPassword("12345678");
    newUser.setImage("null");
    repository.save(newUser);

    LoginUserDto login = new LoginUserDto();
    login.setEmail(newUser.getEmail());
    login.setPassword(newUser.getPassword());
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(login)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Order(2)
  @DisplayName("2 - all values are required")
  void allValuesRequired() throws Exception {
    LoginUserDto firstUser = new LoginUserDto();
    firstUser.setEmail("email_teste@email.com");
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(firstUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    LoginUserDto secondUser = new LoginUserDto();
    secondUser.setPassword("12345678");
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(secondUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(3)
  @DisplayName("3 - email cannot be empty")
  void emptyEmail() throws Exception {
    LoginUserDto login = new LoginUserDto();
    login.setEmail("");
    login.setPassword("12345678");
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(login)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(4)
  @DisplayName("4 - password cannot be empty")
  void emptyPassword() throws Exception {
    LoginUserDto login = new LoginUserDto();
    login.setEmail("email@email.com");
    login.setPassword("");
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(login)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(4)
  @DisplayName("4 - email not found")
  void emailNotFound() throws Exception {
    LoginUserDto login = new LoginUserDto();
    login.setEmail("email@email.com");
    login.setPassword("12345678");
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(login)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
