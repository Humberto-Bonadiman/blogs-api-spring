package com.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.spring.dto.PostDto;
import com.java.spring.exception.TokenNotFoundException;
import com.java.spring.model.Post;
import com.java.spring.service.PostServices;

@RestController
@RequestMapping("/post")
public class PostController {

  @Autowired
  PostServices service;

  @PostMapping
  public ResponseEntity<Post> create(
    @RequestBody PostDto post,
    @RequestHeader(value="token", defaultValue = "") String token
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(post, token));
  }
}
