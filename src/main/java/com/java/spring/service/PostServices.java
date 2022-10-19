package com.java.spring.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.java.spring.dto.CreatePostResultDto;
import com.java.spring.dto.PostDto;
import com.java.spring.dto.UpdatePostDto;
import com.java.spring.dto.UpdatePostResultDto;
import com.java.spring.exception.CategoryNotFoundException;
import com.java.spring.exception.PostNotFoundException;
import com.java.spring.exception.UnauthorizedUserException;
import com.java.spring.model.Categories;
import com.java.spring.model.Post;
import com.java.spring.model.User;
import com.java.spring.repository.CategoriesRepository;
import com.java.spring.repository.PostRepository;
import com.java.spring.repository.UserRepository;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class PostServices {

  @Autowired
  PostRepository repository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CategoriesRepository categoriesRepository;

  @Autowired
  GlobalMethodsService globalService;

  public CreatePostResultDto create(PostDto object, String token) {
    try {
      DecodedJWT decoded = globalService.verifyToken(token);
      Long numberId = globalService.returnIdToken(decoded);
      globalService.verifyToken(token);
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
      User user = userRepository.findById(numberId).get();
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
    try {
      globalService.verifyToken(token);
      return repository.findAll();
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }

  }

  public Post findById(String token, Long id) {
    try {
      globalService.verifyToken(token);
      Optional<Post> isPost = repository.findById(id);
      if (isPost.isEmpty()) throw new PostNotFoundException();
      return repository.findById(id).get();
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  public UpdatePostResultDto update(String token, Long id, UpdatePostDto object) {
    try {
      DecodedJWT decoded = globalService.verifyToken(token);
      Long numberId = globalService.returnIdToken(decoded);
      globalService.verifyToken(token);
      Optional<Post> isPost = repository.findById(id);
      if (isPost.isEmpty()) throw new PostNotFoundException();
      Post post = repository.findById(id).get();
      if (!numberId.equals(post.getUserId())) {
        throw new UnauthorizedUserException();
      }
      post.setTitle(object.getTitle());
      post.setContent(object.getContent());
      post.setUpdated(Clock.systemDefaultZone().instant());
      UpdatePostResultDto returnPost = new UpdatePostResultDto();
      returnPost.setTitle(object.getTitle());
      returnPost.setContent(object.getContent());
      returnPost.setUserId(post.getUserId());
      returnPost.setCategories(post.getCategories());
      return returnPost;
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  public void delete(String token, Long id) {
    try {
      globalService.verifyToken(token);
      DecodedJWT decoded = globalService.verifyToken(token);
      Long numberId = globalService.returnIdToken(decoded);
      Optional<Post> isPost = repository.findById(id);
      if (isPost.isEmpty()) throw new PostNotFoundException();
      Post post = repository.findById(id).get();
      if (!numberId.equals(post.getUserId())) {
        throw new UnauthorizedUserException();
      }
      repository.deleteById(id);
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  public List<Post> findByQueryParamTitleContent(String token, String q) {
    globalService.verifyToken(token);
    if (q == "") {
      return repository.findAll();
    }
    List<Post> findByTitle = repository.findByTitleOrContent(q);
    return findByTitle;
  }
}
