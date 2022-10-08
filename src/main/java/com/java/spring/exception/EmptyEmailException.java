package com.java.spring.exception;

public class EmptyEmailException extends RuntimeException  {
  private static final long serialVersionUID = 1L;

  public EmptyEmailException() {
    super("'email' is not allowed to be empty");
  }

}
