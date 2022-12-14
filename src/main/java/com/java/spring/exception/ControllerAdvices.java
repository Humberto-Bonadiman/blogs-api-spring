package com.java.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTVerificationException;

@ControllerAdvice
public class ControllerAdvices {

  @ExceptionHandler({
      IncorrectEmailFormat.class,
      DisplayNameLengthException.class,
      PasswordLengthException.class,
      NullPointerException.class,
      EmailNotFoundException.class,
      EmptyEmailException.class,
      EmptyPasswordException.class,
      CategoryNotFoundException.class,
  })
  public ResponseEntity<Object> handlerBadRequest(RuntimeException exception) {
    return new ResponseEntity<>(new DataError(exception.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({EmailAlreadyExistException.class})
  public ResponseEntity<Object> handlerConflict(RuntimeException exception) {
    return new ResponseEntity<>(new DataError(exception.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler({
      JWTVerificationException.class,
      TokenNotFoundException.class,
      UnauthorizedUserException.class
  })
  public ResponseEntity<Object> handlerUnauthorized(RuntimeException exception) {
    return new ResponseEntity<>(new DataError(exception.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({UserNotFoundException.class, PostNotFoundException.class})
  public ResponseEntity<Object> handlerNotFound(RuntimeException exception) {
    return new ResponseEntity<>(new DataError(exception.getMessage()), HttpStatus.NOT_FOUND);
  }
}
