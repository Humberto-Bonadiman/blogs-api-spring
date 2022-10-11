package com.java.spring.exception;

public class CategoryNotFoundException extends RuntimeException  {
  private static final long serialVersionUID = 1L;

  public CategoryNotFoundException() {
    super("'categoryIds' not found");
  }
}
