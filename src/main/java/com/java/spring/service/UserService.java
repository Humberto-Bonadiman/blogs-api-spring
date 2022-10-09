package com.java.spring.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.java.spring.dto.CreateUserDto;
import com.java.spring.exception.DisplayNameLengthException;
import com.java.spring.exception.EmailAlreadyExistException;
import com.java.spring.exception.IncorrectEmailFormat;
import com.java.spring.exception.PasswordLengthException;
import com.java.spring.exception.TokenNotFoundException;
import com.java.spring.exception.UserNotFoundException;
import com.java.spring.model.User;
import com.java.spring.repository.UserRepository;

@Service
@Component
public class UserService implements ServiceInterface<CreateUserDto, User> {

  @Autowired
  UserRepository repository;

  @Override
  public User create(CreateUserDto user) {
    try {
      if (!isValidEmailAddress(user.getEmail())) throw new IncorrectEmailFormat();
      if (!isValidDisplayNameLength(user.getDisplayName())) throw new DisplayNameLengthException();
      if (!isValidPasswordLength(user.getPassword())) throw new PasswordLengthException();
      Optional<User> findEmail = repository.findByEmail(user.getEmail());
      if (findEmail.isPresent()) throw new EmailAlreadyExistException();
      User newUser = new User();
      newUser.setDisplayName(user.getDisplayName());
      newUser.setEmail(user.getEmail());
      newUser.setPassword(user.getPassword());
      newUser.setImage(user.getImage());
      return repository.save(newUser);
    } catch(NullPointerException e) {
      throw new NullPointerException("all values is required");
	}
  }

  @Override
  public List<User> findAll(String token) {
    try {
      verifyToken(token);
      return repository.findAll();
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  @Override
  public User findById(Long id, String token) {
    try {
      verifyToken(token);
      Optional<User> user = repository.findById(id);
      if (user.isEmpty()) throw new UserNotFoundException();
      return user.get();
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  public static boolean isValidEmailAddress(String email) {
    if (email == null) throw new NullPointerException("all values is required");
    boolean valid = EmailValidator.getInstance().isValid(email);
    return valid;
  }

  public static boolean isValidDisplayNameLength(String displayName) {
    if (displayName.length() >= 8) return true;
    return false;
  }

  public static boolean isValidPasswordLength(String password) {
    if(password.length() >= 6) return true;
    return false;
  }

  public static DecodedJWT verifyToken(String token) {
    if (token.equals("")) throw new TokenNotFoundException();
    Algorithm algorithm = Algorithm.HMAC256(System.getenv("SECRET"));  
    JWTVerifier verifier = JWT.require(algorithm).build();
    return verifier.verify(token);
  }
}
