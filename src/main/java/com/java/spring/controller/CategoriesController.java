package com.java.spring.controller;

import com.java.spring.dto.CategoriesDto;
import com.java.spring.exception.TokenNotFoundException;
import com.java.spring.model.Categories;
import com.java.spring.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

  @Autowired
  CategoriesService service;

  @PostMapping
  public ResponseEntity<Categories> create(
    @RequestHeader(value="token", defaultValue = "") String token,
    @RequestBody CategoriesDto category
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(token, category.getName()));
  }
}
