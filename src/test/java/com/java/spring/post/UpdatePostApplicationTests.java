package com.java.spring.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;

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
import com.java.spring.dto.TokenDto;
import com.java.spring.dto.UpdatePostDto;
import com.java.spring.model.Categories;
import com.java.spring.model.Post;
import com.java.spring.model.User;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;
import com.java.spring.repository.UserRepository;
import com.java.spring.service.LoginService;

@SpringBootTest
@AutoConfigureMockMvc
class UpdatePostApplicationTests {

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

  @Test
  @Order(1)
  @DisplayName("1 - find a post by id successfully")
  void findPostByIdSuccessfully() throws Exception {
    User user = new User();
    user.setDisplayName("Usuário de teste");
    user.setEmail("email_teste@email.com");
    user.setPassword("12345678");
    user.setImage("null");
    repository.save(user);
    Categories category = new Categories();
    category.setName("Spring-boot");
    categoriesRepository.save(category);
    Post post = new Post();
    post.setTitle("Start Java Today");
    post.setContent("Learn java in 6 months");
    post.setUserId(user.getId());
    post.setPublished(Clock.systemDefaultZone().instant());
    post.setUpdated(Clock.systemDefaultZone().instant());
    post.setUser(user);
    postRepository.save(post);
    LoginUserDto login = new LoginUserDto();
    login.setEmail("email_teste@email.com");
    login.setPassword("12345678");
    TokenDto token = loginService.findByEmail(login);
    UpdatePostDto update = new UpdatePostDto();
    update.setTitle("Start Java Now");
    update.setContent("Learn java in 4 months");
    mockMvc.perform(put("/post/" + post.getId())
        .header("token", token.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(update)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
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
    Post post = new Post();
    post.setTitle("Start Java Today");
    post.setContent("Learn java in 6 months");
    post.setUserId(user.getId());
    post.setPublished(Clock.systemDefaultZone().instant());
    post.setUpdated(Clock.systemDefaultZone().instant());
    post.setUser(user);
    postRepository.save(post);
    UpdatePostDto update = new UpdatePostDto();
    update.setTitle("Start Java Now");
    update.setContent("Learn java in 4 months");
    
    mockMvc.perform(put("/post/" + post.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(update)))
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
    Post post = new Post();
    post.setTitle("Start Java Today");
    post.setContent("Learn java in 6 months");
    post.setUserId(user.getId());
    post.setPublished(Clock.systemDefaultZone().instant());
    post.setUpdated(Clock.systemDefaultZone().instant());
    post.setUser(user);
    postRepository.save(post);
    UpdatePostDto update = new UpdatePostDto();
    update.setTitle("Start Java Now");
    update.setContent("Learn java in 4 months");
    mockMvc.perform(put("/post/1").header("token", "abcd1525")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(update)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(4)
  @DisplayName("4 - post not found")
  void postNotFound() throws Exception {
    User user = new User();
    user.setDisplayName("Usuário de teste");
    user.setEmail("email_teste@email.com");
    user.setPassword("12345678");
    user.setImage("null");
    repository.save(user);
    Categories category = new Categories();
    category.setName("Spring-boot");
    categoriesRepository.save(category);
    Post post = new Post();
    post.setTitle("Start Java Today");
    post.setContent("Learn java in 6 months");
    post.setUserId(user.getId());
    post.setPublished(Clock.systemDefaultZone().instant());
    post.setUpdated(Clock.systemDefaultZone().instant());
    post.setUser(user);
    postRepository.save(post);
    LoginUserDto login = new LoginUserDto();
    login.setEmail("email_teste@email.com");
    login.setPassword("12345678");
    TokenDto token = loginService.findByEmail(login);
    UpdatePostDto update = new UpdatePostDto();
    update.setTitle("Start Java Now");
    update.setContent("Learn java in 4 months");
    mockMvc.perform(put("/post/" + post.getId() + 10)
        .header("token", token.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(update)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(5)
  @DisplayName("5 - user not authorized")
  void userNotAuthorized() throws Exception {
    User user = new User();
    user.setDisplayName("Usuário de teste");
    user.setEmail("email_teste@email.com");
    user.setPassword("12345678");
    user.setImage("null");
    repository.save(user);
    User secondUser = new User();
    secondUser.setDisplayName("Segundo Usuário de teste");
    secondUser.setEmail("segundo_teste@email.com");
    secondUser.setPassword("12345678");
    secondUser.setImage("null");
    repository.save(secondUser);
    Categories category = new Categories();
    category.setName("Spring-boot");
    categoriesRepository.save(category);
    Post post = new Post();
    post.setTitle("Start Java Today");
    post.setContent("Learn java in 6 months");
    post.setUserId(user.getId());
    post.setPublished(Clock.systemDefaultZone().instant());
    post.setUpdated(Clock.systemDefaultZone().instant());
    post.setUser(user);
    postRepository.save(post);
    LoginUserDto login = new LoginUserDto();
    login.setEmail("segundo_teste@email.com");
    login.setPassword("12345678");
    TokenDto token = loginService.findByEmail(login);
    UpdatePostDto update = new UpdatePostDto();
    update.setTitle("Start Java Now");
    update.setContent("Learn java in 4 months");
    mockMvc.perform(put("/post/" + post.getId())
        .header("token", token.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(update)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }
}
