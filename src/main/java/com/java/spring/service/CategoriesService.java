package com.java.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.java.spring.model.Categories;
import com.java.spring.repository.CategoriesRepository;

@Service
@Component
public class CategoriesService {

  @Autowired
  CategoriesRepository repository;

  @Autowired
  UserService userService;

  public Categories create(String token, String name) {
    try {
      if (name == null) throw new NullPointerException("all values is required");
      userService.verifyToken(token);
      Categories category = new Categories();
      category.setName(name);
      return repository.save(category);
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }
}
