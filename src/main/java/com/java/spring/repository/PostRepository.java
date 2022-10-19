package com.java.spring.repository;

import com.java.spring.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Query(value = "SELECT t FROM Post t WHERE t.title LIKE %?1% OR t.content LIKE %?1%")
  List<Post> findByTitleOrContent(String q);
}
