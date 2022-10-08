package com.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.spring.dto.LoginUserDto;
import com.java.spring.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

  @Autowired
  LoginService service;

  @PostMapping
  public ResponseEntity<String> create(@RequestBody LoginUserDto object) {
    return ResponseEntity.status(HttpStatus.OK).body(service.findByEmail(object));
  }
}
