package com.java.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.spring.dto.CreatePostResultDto;
import com.java.spring.dto.PostDto;
import com.java.spring.dto.UpdatePostDto;
import com.java.spring.dto.UpdatePostResultDto;
import com.java.spring.exception.TokenNotFoundException;
import com.java.spring.model.Post;
import com.java.spring.service.PostServices;

@RestController
@RequestMapping("/post")
public class PostController {

  @Autowired
  PostServices service;

  @PostMapping
  public ResponseEntity<CreatePostResultDto> create(
    @RequestBody PostDto post,
    @RequestHeader(value="token", defaultValue = "") String token
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(post, token));
  }

  @GetMapping
  public ResponseEntity<List<Post>> findAll(@RequestHeader(value="token", defaultValue = "") String token) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.OK).body(service.findAll(token));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Post> findById(
    @RequestHeader(value="token", defaultValue = "") String token,
    @PathVariable Long id
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.OK).body(service.findById(token, id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UpdatePostResultDto> update(
    @RequestHeader(value="token", defaultValue = "") String token,
    @PathVariable Long id,
    @RequestBody UpdatePostDto object
  ) {
    if (token == "") throw new TokenNotFoundException();
    return ResponseEntity.status(HttpStatus.OK).body(service.update(token, id, object));
  }
}
