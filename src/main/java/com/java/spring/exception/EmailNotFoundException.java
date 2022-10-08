package com.java.spring.exception;

public class EmailNotFoundException extends RuntimeException  {
  private static final long serialVersionUID = 1L;

  public EmailNotFoundException() {
    super("Invalid fields");
  }
}
