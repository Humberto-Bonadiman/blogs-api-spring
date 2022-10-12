package com.java.spring.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.java.spring.dto.CreatePostResultDto;
import com.java.spring.dto.PostDto;
import com.java.spring.exception.CategoryNotFoundException;
import com.java.spring.exception.PostNotFoundException;
import com.java.spring.model.Categories;
import com.java.spring.model.Post;
import com.java.spring.model.User;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class PostServices {

  @Autowired
  PostRepository repository;

  @Autowired
  CategoriesRepository categoriesRepository;

  @Autowired
  UserService userService;

  public CreatePostResultDto create(PostDto object, String token) {
    try {
      DecodedJWT decoded = userService.verifyToken(token);
      Long numberId = returnIdToken(decoded);
      userService.verifyToken(token);
      if (object.getTitle() == null
          || object.getContent() == null
          || object.getCategoryIds() == null) {
        throw new NullPointerException("all values is required");
      }
      Post newPost = new Post();
      newPost.setTitle(object.getTitle());
      newPost.setContent(object.getContent());
      newPost.setUserId(numberId);
      newPost.setPublished(Clock.systemDefaultZone().instant());
      newPost.setUpdated(Clock.systemDefaultZone().instant());
      User user = userService.findById(numberId, token);
      newPost.setUser(user);
      for(Long id: object.getCategoryIds()) {
        Optional<Categories> category = categoriesRepository.findById(id);
        if (category.isEmpty()) throw new CategoryNotFoundException();
        Categories categories = categoriesRepository.findById(id).get();
        newPost.addCategories(categories);
      }
      repository.save(newPost);
      CreatePostResultDto result = new CreatePostResultDto(
          newPost.getId(), newPost.getUserId(), newPost.getTitle(), newPost.getContent());
      return result;
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  public List<Post> findAll(String token) {
    userService.verifyToken(token);
    return repository.findAll();
  }

  public Post findById(String token, Long id) {
    try {
      userService.verifyToken(token);
      return repository.findById(id).get();
    } catch (Exception e) {
      throw new PostNotFoundException();
    }
  }

  public Long returnIdToken(DecodedJWT decoded) {
    String encPayload = decoded.getPayload();
    String payload = decode(encPayload);
    String firstPartPayload = payload.substring(payload.indexOf("id") + 4);
    return Long.parseLong(
        firstPartPayload.substring(0, firstPartPayload.indexOf(","))); 
  }

  public String decode(final String base64) {
    return StringUtils.newStringUtf8(Base64.decodeBase64(base64));
  }
}
