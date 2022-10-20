package com.java.spring.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;

import com.java.spring.dto.LoginUserDto;
import com.java.spring.dto.TokenDto;
import com.java.spring.model.Categories;
import com.java.spring.model.Post;
import com.java.spring.model.User;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;
import com.java.spring.repository.UserRepository;
import com.java.spring.service.LoginService;

@SpringBootTest
@AutoConfigureMockMvc
class FindPostByIdApplicationTests {

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
    mockMvc.perform(get("/post/" + post.getId())
        .header("token", token.getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value(post.getTitle()))
        .andExpect(jsonPath("$.content").value(post.getContent()))
        .andExpect(jsonPath("$.userId").value(post.getUserId()))
        .andExpect(jsonPath("$.user.id").value(user.getId()))
        .andExpect(jsonPath("$.user.displayName").value(user.getDisplayName()))
        .andExpect(jsonPath("$.user.email").value(user.getEmail()))
        .andExpect(jsonPath("$.user.image").value(user.getImage()));
  }

  @Test
  @Order(2)
  @DisplayName("2 - throws a error if token is abscent")
  void throwsErrorWithoutToken() throws Exception {
    mockMvc.perform(get("/post/1"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(3)
  @DisplayName("3 - throws a error if token is invalid")
  void invalidToken() throws Exception {
    mockMvc.perform(get("/post/1").header("token", "abcd1525"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(4)
  @DisplayName("4 - throws an error if post is not found")
  void postNotFosund() throws Exception {
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
    mockMvc.perform(get("/post/1500")
        .header("token", token.getToken()))
        .andExpect(status().isNotFound());
  }
}
