package com.java.spring.service;

import java.util.Optional;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.java.spring.dto.CreateUserDto;
import com.java.spring.exception.DisplayNameLengthException;
import com.java.spring.exception.EmailAlreadyExistException;
import com.java.spring.exception.IncorrectEmailFormat;
import com.java.spring.exception.PasswordLengthException;
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
}
