package com.java.spring.service;

import java.util.List;

public interface CategoriesServiceInterface<T, K> {

  public K create(String token, T object);

  public List<K> findAll(String token);

}
