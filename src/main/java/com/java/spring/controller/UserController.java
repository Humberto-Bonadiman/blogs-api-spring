package com.java.spring.controller;

import com.java.spring.dto.CreateUserDto;
import com.java.spring.exception.TokenNotFoundException;
import com.java.spring.model.User;
import com.java.spring.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserService service;

  @PostMapping
  public ResponseEntity<User> create(@RequestBody CreateUserDto object) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(object));
  }

  @GetMapping
  public ResponseEntity<List<User>> findAll(
      @RequestHeader(value="token", defaultValue = "") String token
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.OK).body(service.findAll(token));
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(
    @PathVariable Long id,
    @RequestHeader(value="token", defaultValue = "") String token
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.OK).body(service.findById(id, token));
  }
}
