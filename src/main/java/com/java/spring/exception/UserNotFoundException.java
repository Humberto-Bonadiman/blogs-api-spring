package com.java.spring.exception;

public class UserNotFoundException extends RuntimeException  {
  private static final long serialVersionUID = 1L;

  public UserNotFoundException() {
    super("User does not exist");
  }

}
