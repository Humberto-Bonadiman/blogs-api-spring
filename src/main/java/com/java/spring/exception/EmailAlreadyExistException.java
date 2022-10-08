package com.java.spring.exception;

public class EmailAlreadyExistException extends RuntimeException  {
  private static final long serialVersionUID = 1L;

  public EmailAlreadyExistException() {
    super("User already registered");
  }
}
