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

import com.java.spring.dto.CreateUserDto;
import com.java.spring.model.User;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;
import com.java.spring.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class CreateUserApplicationTests {

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
  
  String imageFirst = "https://e7.pngegg.com/pngimages/335/197/png-clipart-computer";
  String imageSecond = "-icons-google-account-user-email-miscellaneous-rim.png";

  @Test
  @Order(1)
  @DisplayName("1 - Must register an user successfully")
  void createUserSuccessfully() throws Exception {
    CreateUserDto newUser = new CreateUserDto();
    newUser.setDisplayName("Usuário de teste");
    newUser.setEmail("email_teste@email.com");
    newUser.setPassword("12345678");
    newUser.setImage(imageFirst + imageSecond);
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(newUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  @Order(2)
  @DisplayName("2 - Throws an error if email format is invalid")
  void emailFormatIsInvalid() throws Exception {
    CreateUserDto firstUser = new CreateUserDto();
    firstUser.setEmail("@email.com");
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(firstUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    CreateUserDto secondUser = new CreateUserDto();
    secondUser.setEmail("email_testeemail.com");
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(secondUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    CreateUserDto thirdUser = new CreateUserDto();
    thirdUser.setEmail("email_teste@");
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(thirdUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(3)
  @DisplayName("3 - displayName length cannot be less than 8")
  void wrongDisplayNameLength() throws Exception {
    CreateUserDto user = new CreateUserDto();
    user.setDisplayName("Usuário");
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(4)
  @DisplayName("4 - password length cannot be less than 6")
  void wrongPasswordLength() throws Exception {
    CreateUserDto user = new CreateUserDto();
    user.setPassword("12345");
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(5)
  @DisplayName("5 - it is not possible to register an email already registered")
  void emailAlreadyRegistered() throws Exception {
    User newUser = new User();
    newUser.setDisplayName("Usuário de teste");
    newUser.setEmail("email_teste@email.com");
    newUser.setPassword("12345678");
    newUser.setImage(imageFirst + imageSecond);
    repository.save(newUser);

    CreateUserDto secondUser = new CreateUserDto();
    secondUser.setEmail("email_teste@email.com");
    secondUser.setDisplayName("Novo Usuário de teste");
    secondUser.setPassword("1234567890");
    secondUser.setImage(imageFirst + imageSecond);
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(secondUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
  }

  @Test
  @Order(6)
  @DisplayName("6 - all values are required")
  void allValuesRequired() throws Exception {
    CreateUserDto newUser = new CreateUserDto();
    newUser.setEmail("email_teste@email.com");
    newUser.setPassword("12345678");
    newUser.setImage(imageFirst + imageSecond);
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(newUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    CreateUserDto secondUser = new CreateUserDto();
    secondUser.setDisplayName("Usuário de teste");
    secondUser.setPassword("12345678");
    secondUser.setImage(imageFirst + imageSecond);
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(secondUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    CreateUserDto thirdUser = new CreateUserDto();
    thirdUser.setDisplayName("Usuário de teste");
    thirdUser.setEmail("email_teste@email.com");
    thirdUser.setImage(imageFirst + imageSecond);
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(thirdUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    CreateUserDto fourthUser = new CreateUserDto();
    fourthUser.setDisplayName("Usuário de teste");
    fourthUser.setEmail("email_teste@email.com");
    fourthUser.setPassword("12345678");
    mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(fourthUser)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
