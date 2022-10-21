package com.java.spring.controller;

import com.java.spring.dto.CreateUserDto;
import com.java.spring.exception.TokenNotFoundException;
import com.java.spring.model.User;
import com.java.spring.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Account endpoint")
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserService service;

  @Operation(summary = "Create an user")
  @PostMapping
  public ResponseEntity<User> create(@RequestBody CreateUserDto object) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(object));
  }

  @Operation(summary = "List all users")
  @GetMapping
  public ResponseEntity<List<User>> findAll(
      @RequestHeader(value="token", defaultValue = "") String token
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.OK).body(service.findAll(token));
  }

  @Operation(summary = "List an user by your id")
  @GetMapping("/{id}")
  public ResponseEntity<User> findById(
    @PathVariable Long id,
    @RequestHeader(value="token", defaultValue = "") String token
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.OK).body(service.findById(id, token));
  }

  @Operation(summary = "Delete an user by your token")
  @DeleteMapping("/me")
  public ResponseEntity<Object> deleteMe(@RequestHeader(value="token", defaultValue = "") String token) {
    if (token == "") throw new TokenNotFoundException();
    service.deleteMe(token);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
