package com.java.spring.exception;

public class UnauthorizedUserException extends RuntimeException  {
  private static final long serialVersionUID = 1L;

  public UnauthorizedUserException() {
    super("Unauthorized user");
  }

}
