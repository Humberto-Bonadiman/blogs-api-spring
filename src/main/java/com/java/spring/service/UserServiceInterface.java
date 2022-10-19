package com.java.spring.service;

import java.util.List;

public interface UserServiceInterface<T, K> {

  public K create(T object);

  public List<K> findAll(String token);

  public K findById(Long id, String token);

  public void deleteMe(String token);
}
