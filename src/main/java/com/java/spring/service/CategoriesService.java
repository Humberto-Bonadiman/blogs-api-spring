package com.java.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.java.spring.dto.CategoriesDto;
import com.java.spring.model.Categories;
import com.java.spring.repository.CategoriesRepository;

@Service
@Component
public class CategoriesService implements CategoriesServiceInterface<CategoriesDto, Categories> {

  @Autowired
  CategoriesRepository repository;

  @Autowired
  GlobalMethodsService globalService;

  @Override
  public Categories create(CategoriesDto category, String token) {
    try {
      if (category.getName() == null) throw new NullPointerException("all values is required");
      globalService.verifyToken(token);
      Categories newCategory = new Categories();
      newCategory.setName(category.getName());
      return repository.save(newCategory);
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  @Override
  public List<Categories> findAll(String token) {
    try {
      globalService.verifyToken(token);
      return repository.findAll();
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    } 
  }
}
