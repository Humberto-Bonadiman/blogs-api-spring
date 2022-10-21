package com.java.spring.controller;

import com.java.spring.dto.CategoriesDto;
import com.java.spring.exception.TokenNotFoundException;
import com.java.spring.model.Categories;
import com.java.spring.service.CategoriesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Categories endpoint")
@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoriesController {

  @Autowired
  CategoriesService service;

  @Operation(summary = "Create a category")
  @PostMapping
  public ResponseEntity<Categories> create(
    @RequestBody CategoriesDto category,
    @RequestHeader(value="token", defaultValue = "") String token
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(category, token));
  }

  @Operation(summary = "List all categories")
  @GetMapping
  public ResponseEntity<List<Categories>> findAll(
    @RequestHeader(value="token", defaultValue = "") String token
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.OK).body(service.findAll(token));
  }
}
