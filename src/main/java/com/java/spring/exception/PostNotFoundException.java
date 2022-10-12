package com.java.spring.exception;

public class PostNotFoundException extends RuntimeException  {
  private static final long serialVersionUID = 1L;

  public PostNotFoundException() {
    super("Post does not exist");
  }

}
