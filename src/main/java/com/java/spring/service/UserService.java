package com.java.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.java.spring.dto.CreateUserDto;
import com.java.spring.exception.DisplayNameLengthException;
import com.java.spring.exception.EmailAlreadyExistException;
import com.java.spring.exception.IncorrectEmailFormat;
import com.java.spring.exception.PasswordLengthException;
import com.java.spring.exception.UserNotFoundException;
import com.java.spring.model.User;
import com.java.spring.repository.UserRepository;

@Service
@Component
public class UserService implements UserServiceInterface<CreateUserDto, User> {

  @Autowired
  UserRepository repository;

  @Autowired
  GlobalMethodsService globalService;

  @Autowired
  PostServices postServices;

  @Override
  public User create(CreateUserDto user) {
    try {
      if (!GlobalMethodsService.isValidEmailAddress(user.getEmail())) throw new IncorrectEmailFormat();
      if (!globalService.isValidDisplayNameLength(user.getDisplayName())) throw new DisplayNameLengthException();
      if (!globalService.isValidPasswordLength(user.getPassword())) throw new PasswordLengthException();
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
      globalService.verifyToken(token);
      return repository.findAll();
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  @Override
  public User findById(Long id, String token) {
    try {
      globalService.verifyToken(token);
      Optional<User> user = repository.findById(id);
      if (user.isEmpty()) throw new UserNotFoundException();
      return user.get();
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

  @Override
  public void deleteMe(String token) {
    try {
      globalService.verifyToken(token);
      DecodedJWT decoded = globalService.verifyToken(token);
      Long numberId = globalService.returnIdToken(decoded);
      repository.deleteById(numberId);
    } catch (JWTVerificationException exception){
      throw new JWTVerificationException("Expired or invalid token");
    }
  }

}
