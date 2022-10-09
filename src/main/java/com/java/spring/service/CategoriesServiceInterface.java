package com.java.spring.service;

import java.util.List;

public interface CategoriesServiceInterface<T, K> {

  public K create(T object, String token);

  public List<K> findAll(String token);

}
