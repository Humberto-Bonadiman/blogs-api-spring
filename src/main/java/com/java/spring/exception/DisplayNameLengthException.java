package com.java.spring.exception;

public class DisplayNameLengthException extends RuntimeException  {
  private static final long serialVersionUID = 1L;

  public DisplayNameLengthException() {
    super("'displayName' length must be at least 8 characters long");
  }
}
