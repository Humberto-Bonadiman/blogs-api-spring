package com.java.spring.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import com.java.spring.dto.CategoriesDto;
import com.java.spring.dto.LoginUserDto;
import com.java.spring.dto.PostDto;
import com.java.spring.dto.TokenDto;
import com.java.spring.model.Categories;
import com.java.spring.model.User;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;
import com.java.spring.repository.UserRepository;
import com.java.spring.service.LoginService;

@SpringBootTest
@AutoConfigureMockMvc
class CreatePostApplicationTests {

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
  @DisplayName("1 - create post successfully")
  void createPostSuccessfully() throws Exception {
    User user = new User();
    user.setDisplayName("Usuário de teste");
    user.setEmail("email_teste@email.com");
    user.setPassword("12345678");
    user.setImage("null");
    repository.save(user);
    Categories category = new Categories();
    category.setName("Spring-boot");
    categoriesRepository.save(category);
    PostDto postDto = new PostDto();
    postDto.setTitle("Start Java Today");
    postDto.setContent("Learn java in 6 months");
    List<Long> ids = new ArrayList<>();
    ids.add(category.getId());
    postDto.setCategoryIds(ids);
    LoginUserDto login = new LoginUserDto();
    login.setEmail("email_teste@email.com");
    login.setPassword("12345678");
    TokenDto token = loginService.findByEmail(login);
    mockMvc.perform(post("/post")
        .header("token", token.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(postDto)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  @Order(2)
  @DisplayName("2 - throws a error if token is abscent")
  void throwsErrorWithoutToken() throws Exception {
    User user = new User();
    user.setDisplayName("Usuário de teste");
    user.setEmail("email_teste@email.com");
    user.setPassword("12345678");
    user.setImage("null");
    repository.save(user);
    Categories category = new Categories();
    category.setName("Spring-boot");
    categoriesRepository.save(category);
    PostDto postDto = new PostDto();
    postDto.setTitle("Start Java Today");
    postDto.setContent("Learn java in 6 months");
    List<Long> ids = new ArrayList<>();
    ids.add(category.getId());
    postDto.setCategoryIds(ids);
    mockMvc.perform(post("/post")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(postDto)))    
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(3)
  @DisplayName("3 - throws a error if token is invalid")
  void invalidToken() throws Exception {
    User user = new User();
    user.setDisplayName("Usuário de teste");
    user.setEmail("email_teste@email.com");
    user.setPassword("12345678");
    user.setImage("null");
    repository.save(user);
    Categories category = new Categories();
    category.setName("Spring-boot");
    categoriesRepository.save(category);
    PostDto postDto = new PostDto();
    postDto.setTitle("Start Java Today");
    postDto.setContent("Learn java in 6 months");
    List<Long> ids = new ArrayList<>();
    ids.add(category.getId());
    postDto.setCategoryIds(ids);
    mockMvc.perform(post("/post").header("token", "abcd1525")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(postDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(4)
  @DisplayName("4 - all values are required")
  void allValuesRequired() throws Exception {
    User user = new User();
    user.setDisplayName("Usuário de teste");
    user.setEmail("email_teste@email.com");
    user.setPassword("12345678");
    user.setImage("null");
    repository.save(user);
    Categories category = new Categories();
    category.setName("Spring-boot");
    categoriesRepository.save(category);
    LoginUserDto login = new LoginUserDto();
    login.setEmail("email_teste@email.com");
    login.setPassword("12345678");
    TokenDto token = loginService.findByEmail(login);
    PostDto postDto = new PostDto();
    postDto.setContent("Learn java in 6 months");
    List<Long> ids = new ArrayList<>();
    ids.add(category.getId());
    postDto.setCategoryIds(ids);
    mockMvc.perform(post("/post")
        .header("token", token.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(postDto)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    PostDto secondPost = new PostDto();
    secondPost.setTitle("Start Java Today");
    secondPost.setCategoryIds(ids);
    mockMvc.perform(post("/post")
        .header("token", token.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(secondPost)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    PostDto thirdPost = new PostDto();
    thirdPost.setTitle("Start Java Today");
    thirdPost.setContent("Learn java in 6 months");
    mockMvc.perform(post("/post")
        .header("token", token.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(thirdPost)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(5)
  @DisplayName("5 - category not found")
  void categoryNotFound() throws Exception {
    User user = new User();
    user.setDisplayName("Usuário de teste");
    user.setEmail("email_teste@email.com");
    user.setPassword("12345678");
    user.setImage("null");
    repository.save(user);
    Categories category = new Categories();
    category.setName("Spring-boot");
    categoriesRepository.save(category);
    PostDto postDto = new PostDto();
    postDto.setTitle("Start Java Today");
    postDto.setContent("Learn java in 6 months");
    List<Long> ids = new ArrayList<>();
    ids.add(category.getId() + 10);
    postDto.setCategoryIds(ids);
    LoginUserDto login = new LoginUserDto();
    login.setEmail("email_teste@email.com");
    login.setPassword("12345678");
    TokenDto token = loginService.findByEmail(login);
    mockMvc.perform(post("/post")
        .header("token", token.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(postDto)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
