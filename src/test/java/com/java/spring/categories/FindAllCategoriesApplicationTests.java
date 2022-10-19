package com.java.spring.categories;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import com.java.spring.model.Categories;
import com.java.spring.model.User;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;
import com.java.spring.repository.UserRepository;
import com.java.spring.service.LoginService;

@SpringBootTest
@AutoConfigureMockMvc
class FindAllCategoriesApplicationTests {

  @Autowired
  MockMvc mockMvc;

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

  @Test
  @Order(1)
  @DisplayName("1 - find all categories successfully")
  void findAllCategories() throws Exception {
    User user = new User();
    user.setDisplayName("Usuário de teste");
    user.setEmail("email_teste@email.com");
    user.setPassword("12345678");
    user.setImage("null");
    repository.save(user);
    LoginUserDto login = new LoginUserDto();
    login.setEmail(user.getEmail());
    login.setPassword(user.getPassword());
    TokenDto token = loginService.findByEmail(login);
    Categories category = new Categories();
    category.setName("Spring-boot");
    categoriesRepository.save(category);
    Categories second = new Categories();
    second.setName("Java");
    categoriesRepository.save(second);
    mockMvc.perform(get("/categories")
        .header("token", token.getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value(category.getName()))
        .andExpect(jsonPath("$[1].name").value(second.getName()));
  }

  @Test
  @Order(2)
  @DisplayName("2 - throws a error if token is abscent")
  void throwsErrorWithoutToken() throws Exception {
    mockMvc.perform(get("/categories"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(3)
  @DisplayName("3 - throws a error if token is invalid")
  void invalidToken() throws Exception {
    mockMvc.perform(get("/categories").header("token", "abcd1525"))
      .andExpect(status().isUnauthorized());
  }
}
