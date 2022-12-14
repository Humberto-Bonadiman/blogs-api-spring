package com.java.spring.repository;

import com.java.spring.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);
  public Optional<User> findByEmailAndPassword(String email, String password);
}
