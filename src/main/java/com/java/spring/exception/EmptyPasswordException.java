package com.java.spring.exception;

public class EmptyPasswordException extends RuntimeException  {
  private static final long serialVersionUID = 1L;

  public EmptyPasswordException() {
    super("'password' is not allowed to be empty");
  }

}
