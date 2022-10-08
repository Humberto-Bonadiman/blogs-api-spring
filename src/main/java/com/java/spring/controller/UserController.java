package com.java.spring.controller;

import com.java.spring.dto.CreateUserDto;
import com.java.spring.model.User;
import com.java.spring.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
}
