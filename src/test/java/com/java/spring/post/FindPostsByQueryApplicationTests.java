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
import com.java.spring.service.PostServices;

@SpringBootTest
@AutoConfigureMockMvc
class FindPostsByQueryApplicationTests {

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

  @Autowired
  PostServices postService;

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
  @DisplayName("1 - find all posts by query parameter successfully")
  void findAllPostsByQuerySuccessfully() throws Exception {
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
    mockMvc.perform(get("/post/search?q=java")
        .header("token", token.getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value(post.getTitle()))
        .andExpect(jsonPath("$[0].content").value(post.getContent()))
        .andExpect(jsonPath("$[0].userId").value(post.getUserId()))
        .andExpect(jsonPath("$[0].user.id").value(user.getId()))
        .andExpect(jsonPath("$[0].user.displayName").value(user.getDisplayName()))
        .andExpect(jsonPath("$[0].user.email").value(user.getEmail()))
        .andExpect(jsonPath("$[0].user.image").value(user.getImage()));
  }

  @Test
  @Order(2)
  @DisplayName("2 - throws a error if token is abscent")
  void throwsErrorWithoutToken() throws Exception {
    mockMvc.perform(get("/post/search?q=text"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(3)
  @DisplayName("3 - throws a error if token is invalid")
  void invalidToken() throws Exception {
    mockMvc.perform(get("/post/search?q=text").header("token", "abcd1525"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(4)
  @DisplayName("4 - show all posts if query is empty")
  void emptyQuery() throws Exception {
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
    mockMvc.perform(get("/post/search")
        .header("token", token.getToken())
        .param("q", ""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value(post.getTitle()))
        .andExpect(jsonPath("$[0].content").value(post.getContent()))
        .andExpect(jsonPath("$[0].userId").value(post.getUserId()))
        .andExpect(jsonPath("$[0].user.id").value(user.getId()))
        .andExpect(jsonPath("$[0].user.displayName").value(user.getDisplayName()))
        .andExpect(jsonPath("$[0].user.email").value(user.getEmail()))
        .andExpect(jsonPath("$[0].user.image").value(user.getImage()));
  }

  @Test
  @Order(5)
  @DisplayName("5 - show any posts if query is do not match")
  void queryDoNotMatch() throws Exception {
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
    mockMvc.perform(get("/post/search")
        .header("token", token.getToken())
        .param("q", "ztua"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }
}
