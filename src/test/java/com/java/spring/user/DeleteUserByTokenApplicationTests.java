package com.java.spring.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.java.spring.dto.LoginUserDto;
import com.java.spring.dto.TokenDto;
import com.java.spring.model.User;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;
import com.java.spring.repository.UserRepository;
import com.java.spring.service.LoginService;

@SpringBootTest
@AutoConfigureMockMvc
class DeleteUserByTokenApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  UserRepository repository;

  @Autowired
  PostRepository postRepository;

  @Autowired
  CategoriesRepository categoriesRepository;

  @Autowired
  LoginService loginService;

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
  
  String imageFirst = "https://e7.pngegg.com/pngimages/335/197/png-clipart-computer";
  String imageSecond = "-icons-google-account-user-email-miscellaneous-rim.png";

  @Test
  @Order(1)
  @DisplayName("1 - Delete an user by token successfully")
  void createUserSuccessfully() throws Exception {
    User newUser = new User();
    newUser.setDisplayName("Usuário de teste");
    newUser.setEmail("email_teste@email.com");
    newUser.setPassword("12345678");
    newUser.setImage(imageFirst + imageSecond);
    repository.save(newUser);
    LoginUserDto login = new LoginUserDto();
    login.setEmail("email_teste@email.com");
    login.setPassword("12345678");
    TokenDto token = loginService.findByEmail(login);
    mockMvc.perform(delete("/user/me")
        .header("token", token.getToken()))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(2)
  @DisplayName("2 - throws an error without token")
  void tokenNotFound() throws Exception {
    User newUser = new User();
    newUser.setDisplayName("Usuário de teste");
    newUser.setEmail("email_teste@email.com");
    newUser.setPassword("12345678");
    newUser.setImage(imageFirst + imageSecond);
    repository.save(newUser);
    mockMvc.perform(delete("/user/me"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(3)
  @DisplayName("3 - throws an error if token is invalid")
  void invalidToken() throws Exception {
    User newUser = new User();
    newUser.setDisplayName("Usuário de teste");
    newUser.setEmail("email_teste@email.com");
    newUser.setPassword("12345678");
    newUser.setImage(imageFirst + imageSecond);
    repository.save(newUser);
    LoginUserDto login = new LoginUserDto();
    login.setEmail("email_teste@email.com");
    login.setPassword("12345678");
    TokenDto token = loginService.findByEmail(login);
    mockMvc.perform(delete("/user/me")
        .header("token", token.getToken() + "joajivhue82.jfuaww"))
        .andExpect(status().isUnauthorized());
  }
}
