package com.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.spring.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {}
